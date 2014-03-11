package me.valour.bowls;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import me.valour.milktea.*;

public class Bill  extends CrossTable<User, LineItem, Double>{

	private ArrayList<User> users; 
	private ArrayList<LineItem> lineItems;
	private boolean splitEqually;
	
	private double subtotal;

	private double percentTip;
	private double percentTax;
	
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
	
	public void setTax(double tax){
		percentTax = tax;
	}
	
	public void setTip(double tip){
		percentTip = tip;
	}
	
	public LineItem addLineItem(double price){
		LineItem li = new LineItem(price);
		lineItems.add(li);
		addCol(li, users, 0.0);
		subtotal += price;
		return li;
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
	}
	
	public void divideAmongst(LineItem li, List<User> us){
		if(us.isEmpty()){
			return;
		}
		double div = li.getPrice() / us.size();
		for(User u: us){
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
	
	public double calculateTip(){
		for(User u: users){
			u.applyTip(percentTip);
		}
		return subtotal * percentTip;
	}
	
	public double calculateTax(){
		for(User u: users){
			u.applyTax(percentTax);
		}
		return subtotal * percentTax;
	}
	
	public void clearTip(){
		for(User u: users){
			u.setTip(0.0);
		}
	}
	
	public void clearTax(){
		for(User u: users){
			u.setTax(0.0);
		}
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
	
	public ArrayList<LineItem> getLineItems(){
		return lineItems;
	}
	
	public void populateLineItemsWithUsers(){
		for(LineItem li: lineItems){
			li.clearUsers();
			for(User user: users){
				if(get(user, li, 0.0)>0.0){
					li.addUser(user);
				}
			}
		}
	}

}
