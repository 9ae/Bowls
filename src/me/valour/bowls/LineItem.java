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
	
	/**
	 * Add or removes user
	 * @param id BowlView.id
	 * @return true if user is added, false if user is removed
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
	

}
