package me.valour.bowls;

import java.util.ArrayList;

public class LineItem {
	
	/**
	 * Price of item
	 */
	private double price;
	private double previousPrice=0.0;
//	public int shares;
	
	public LineItem(double price){
		this.price = price;
	//	shares = 0;
	}
	
/*	public LineItem(double price, ArrayList<User> users){
		this.price = price;
		shares = users.size();
		double perUser = price/(double)shares;
		for(User u : users){
			u.justAdd(this, perUser);
		}
	}
	*/
	/**
	 * Add or removes user
	 * @param id BowlView.id
	 * @return true if user is added, false if user is removed
	 */
/*	public boolean toggleUser(User u){
		if(u.had(this)){
			u.add(this);
			return false;
		} else{
			u.rm(this);
			return true;
		}
	}
	*/
	public void setPrice(double price){
		this.previousPrice = this.price;
		this.price = price;
	}
	
	public double getPrice(){
		return this.price;
	}
	
	public double getPreviousPrice(){
		return this.previousPrice;
	}
	
	public boolean priceChanged(){
		if(previousPrice<=0.0){
			return false;
		} else {
			return previousPrice!=price;
		}
		
	}
	
	@Override
	public String toString(){
		return String.format("$ %.2f", price);
	}
	
/*	public double pricePerUser(){
		if(shares==0){
			return 0.0;
		} else {
			return price / (double)shares;
		}
	}
	*/
}
