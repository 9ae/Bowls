package me.valour.bowls;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import me.valour.milktea.*;

public class Bill  extends CrossTable<User, LineItem, Double>{

	private ArrayList<User> users; 
	protected ArrayList<LineItem> lineItems;
	private boolean splitEqually;
	
	private double subtotal;

	private double percentTip;
	private double percentTax;
	
	private boolean appliedTip = false;
	private boolean appliedTax = false;
	
	private BillChangesAgent changeAgent;
	
	public Bill(boolean splitEqually, double tax, double tip){
		super();
		this.splitEqually = splitEqually;
		
		users = new ArrayList<User>();
		lineItems = new ArrayList<LineItem>();
		
		subtotal = 0.0;
		percentTax = tax;
		percentTip = tip;
	}
	
	public double getTip(){
		return percentTip;
	}
	
	public double getTax(){
		return percentTax;
	}
	
	public double calculateTip(){
		if(appliedTip){
			return subtotal * percentTip;
		} else {
			return 0.0;
		}
	}
	
	public double calculateTax(){
		if(appliedTax){
			return subtotal * percentTax;
		} else {
			return 0.0;
		}
	}
	
	public void setTax(double tax){
		percentTax = tax;
		changeAgent.taxChanged(true);
	}
	
	public void setTip(double tip){
		percentTip = tip;
		changeAgent.tipChanged(true);
	}
	
	public void applyTip(){
		for(User u: users){
			u.applyTip(percentTip);
		}
		appliedTip = true;
		changeAgent.tipChanged(false);
	//	return subtotal * percentTip;
	}
	
	public void applyTax(){
		for(User u: users){
			u.applyTax(percentTax);
		}
		appliedTax = true;
		changeAgent.taxChanged(false);
	//	return subtotal * percentTax;
	}
	
	public void clearTip(){
		for(User u: users){
			u.setTip(0.0);
		}
		appliedTip = false;
		changeAgent.tipChanged(false);
	}
	
	public void clearTax(){
		for(User u: users){
			u.setTax(0.0);
		}
		appliedTax = false;
		changeAgent.taxChanged(false);
	}
	
	
	public double getSubtotal(){
		return subtotal;
	}
	
	public LineItem addLineItem(double price){
		LineItem li = new LineItem(price);
		lineItems.add(li);
		addCol(li, users, 0.0);
		subtotal += price;
		changeAgent.subtotalChanged();
		return li;
	}
	
	public void updateLineItemPrice(LineItem li, double newPrice){
		subtotal -= li.getPrice();
		subtotal += newPrice;
		li.setPrice(newPrice);
		changeAgent.subtotalChanged();
	}
	
	public void rmLineItem(int index){
		LineItem delItem = lineItems.remove(index);
		double itemPrice = delItem.getPrice();
		subtotal -= itemPrice;
		for(User u: users){
			if(get(u, delItem, 0.0)>0.0){
				u.subtractSubtotal(itemPrice);
			}
		}
		rmCol(delItem);
		changeAgent.subtotalChanged();
	}
	
	public void divideAmongst(LineItem li, List<User> us){
		List<User> prevUsers = listUsers(li); 
		if(us.isEmpty()){
			return;
		}
		double div = li.getPrice() / us.size();
		for(User u: us){
			if(prevUsers.contains(u)){
				u.subtractSubtotal(get(u, li, 0.0));
				prevUsers.remove(u);
			}
			set(u, li, div);
			u.plusSubtotal(div);
		}
		for(User v: prevUsers){
			v.subtractSubtotal(get(v, li, 0.0));
			set(v, li, 0.0);
		}
	}
	
	public void redivideAmongst(LineItem li){
		List<User> us =  listUsers(li); 
		if(us.isEmpty()){
			return;
		}
		double div = li.getPrice() / us.size();
		double pdiv = 0.0;
		if(li.priceChanged()){
			pdiv = li.getPreviousPrice() / us.size();
		}
		for(User u: us){
			if(pdiv!=0.0){
				u.subtractSubtotal(pdiv);
			}
			set(u, li, div);
			u.plusSubtotal(div);
		}
	}
	
	public void divideEqually(LineItem li){
		divideAmongst(li, users);
	}
	
	public void redivideEqually(){
		clearUserSubtotals();
		if(lineItems.size()!=1){
			return;
		} 
		divideEqually(lineItems.get(0));
	}
	
	public void clearUserSubtotals(){
		for(User u: users){
			u.setSubtotal(0.0);
		}
	}
	
	public boolean addUser(User u){
		if(!users.contains(u)){
			users.add(u);
			addRow(u, lineItems, 0.0);
			return true;
		} else {
			return false;
		}
	}
	
	public void addUniqueUsers(List<User> users){
		for(User id: users){
			if(!this.users.contains(id)){
				this.users.add(id);
				addRow(id, lineItems, 0.0);
			}
		}
	}
	
	public boolean allowRmUser(User u){
		return u.getSubtotal()<=0.0;
	}
	
	public void clearUserItems(User u){
		ArrayList<Integer> toRemoveIndices = new ArrayList<Integer>();
		for(Duo<User,LineItem> pair: table.keySet()){
			if(pair.hasRow(u)){
				LineItem li = pair.getCol();
				if(get(u, li, 0.0)>0.0){
					List<User> otherUsers = listOtherUsers(li,u);
					if(otherUsers.size()>0){
						double pricePerUser = li.getPrice()/otherUsers.size();
						for(User ou: otherUsers){
							double oriSplit = get(ou, li, 0.0);
							ou.subtractSubtotal(oriSplit);
							ou.plusSubtotal(pricePerUser);
						}
					} else {
						// remove single
						toRemoveIndices.add(lineItems.indexOf(li));
					}
				}
			}
		}
		for(Integer ind: toRemoveIndices){
			rmLineItem(ind);
		}
	}
	
	private int marginCount(LineItem li){
		int count = 0;
		for(Duo<User,LineItem> pair: table.keySet()){
			if(pair.hasCol(li)){
				
				count++;
			}
		}
		return count;
	}
	
	public List<User> listUsers(LineItem li){
		ArrayList<User> liUsers = new ArrayList<User>();
		for(User user: users){
			if(get(user, li, 0.0)>0.0){
				liUsers.add(user);
			}
		}
		return liUsers;
	}
	
	public List<User> listOtherUsers(LineItem li, User u){
		ArrayList<User> others = new ArrayList<User>();
		for(User user: users){
			if(user==u){ continue; }
			if(get(user, li, 0.0)>0.0){
				others.add(user);
			}
		}
		return others;
	}
	
	
	public void splitEqually(ArrayList<User> users){
		this.splitEqually = true;
		this.users = users;
	}
	
	public double calculateUserTax(User u){
		double t = percentTax * u.getSubtotal();
		u.setTax(t);
		return t;
	}
	
	public double calculateUserTip(User u){
		double t = percentTip * u.getSubtotal();
		u.setTip(t);
		return t;
	}
	
	public void attachAgent(BillFragment fragment){
		changeAgent = (BillChangesAgent)fragment;
	}
	
	public interface BillChangesAgent{
		public void subtotalChanged();
		public void taxChanged(boolean rateChanged);
		public void tipChanged(boolean rateChanged);
		public void updateTotal();
	}

}
