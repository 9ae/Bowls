package me.valour.bowls;

import java.util.ArrayList;

public class User {

	public final int bowlViewId;
	private double subtotal;
	
	private ArrayList<LineItem> items;
	
	public User(int id){
		bowlViewId = id;
		subtotal = 0.0;
		items = new ArrayList<LineItem>();
	}
	
	public boolean had(LineItem item){
		return items.contains(item);
	}
	
	public boolean hadItems(){
		return !items.isEmpty();
	}
	
	public double getSubtotal(){
		return subtotal;
	}
	
	
	public void add(LineItem item){
		item.shares ++;
		subtotal += item.pricePerUser();
		items.add(item);
	}
	
	public void rm(LineItem item){
		subtotal -= item.pricePerUser();
		item.shares --;
		items.remove(item);
	}
	
	public void justAdd(LineItem item, double price){
		//assume shares is set already
		subtotal += price;
		items.add(item);
	}
	
	public void justRm(LineItem item){
		subtotal -= item.pricePerUser();
		items.remove(item);
	}

}
