package me.valour.bowls;

import java.util.Locale;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class LineItem {
	
	/**
	 * Price of item
	 */
	private double price;
	private double previousPrice=0.0;

	public LineItem(double price){
		this.price = price;
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
	
	@SuppressLint("DefaultLocale")
	@Override
	public String toString(){
		return String.format(Locale.US, "$ %.2f", price);
	}
	

}
