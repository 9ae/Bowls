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
	
	public Bill(boolean splitEqually){
		super();
		this.splitEqually = splitEqually;
		
		users = new ArrayList<User>();
		lineItems = new ArrayList<LineItem>();
		
		subtotal = 0.0;
		percentTax = Kitchen.tax;
		percentTip = Kitchen.tip;
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
	
	public User findUserById(int id){
		User u = null;
		for(User user: users){
			if(user.bowlViewId==id){
				u = user;
				break;
			}
		}
		return u;
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
	
	public boolean addUser(int id){
		if(findUserById(id)==null){
			User u = new User(id);
			users.add(u);
			addRow(u, lineItems, 0.0);
			return true;
		} else {
			return false;
		}
	}
	
	public void addUsers(List<Integer> ids){
		for(Integer id: ids){
			addUser(id);
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
		ArrayList<LineItem> toRemove = new ArrayList<LineItem>();
		for(Duo<User,LineItem> pair: table.keySet()){
			if(pair.hasRow(u) && table.get(pair)>0.0){
				LineItem li = pair.getCol();
				int mc = marginCount(li);
				if(mc>1){
					// TODO: redistribute shared
					
				} else {
					// remove single
					toRemove.add(li);
				}
			}
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
	
	public double calculateTip(){
		return subtotal * percentTip;
	}
	
	public double calculateTax(){
		return subtotal * percentTax;
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

}
