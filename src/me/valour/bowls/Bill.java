package me.valour.bowls;

import java.util.ArrayList;

public class Bill {

	private ArrayList<User> users; 
	private ArrayList<LineItem> lineItems;
	private SplitMode splitMode;
	
	private double subtotal;

	private double percentTip;
	private double percentTax;
	
	public Bill(){
		this.splitMode = SplitMode.EACH;
		
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
	
	public void addLineItem(double price){
		LineItem li;
		if(splitMode==SplitMode.EQUALLY){
			li = new LineItem(price, users);
		} else {
			li = new LineItem(price);
		}
		lineItems.add(li);
		subtotal += price;
	}
/*	MAY NOT NEED
	public void addLineItem(double price, ArrayList<User> users){
		LineItem li = new LineItem(price, users);
		addUniqueUsers(users);
		lineItems.add(li);
		subtotal += price;
	}
*/	
	public void addUniqueUsers(ArrayList<User> users){
		for(User id: users){
			if(!this.users.contains(id)){
				this.users.add(id);
			}
		}
	}
	
	public void rmLineItem(int index){
		LineItem delItem = lineItems.remove(index);
		subtotal -= delItem.getPrice();
		for(User u: users){
			u.justRm(delItem);
		}
	}
	
	public double calculateTip(){
		return subtotal * percentTip;
	}
	
	public double calculateTax(){
		return subtotal * percentTax;
	}
	
	public void splitEqually(ArrayList<User> users){
		this.splitMode = SplitMode.EQUALLY;
		this.users = users;
	}
	
	public double calculateUserTax(User u){
		return percentTax * u.getSubtotal();
	}
	
	public double calculateUserTip(User u){
		return percentTip * u.getSubtotal();
	}
	
	public boolean addUser(User u){
		if(!users.contains(u)){
			users.add(u);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean rmUserSetup(User u){
		if(u.hadItems()){
			return false;
		} else {
			users.remove(u);
			return true;
		}
	}

}
