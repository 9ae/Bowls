package me.valour.bowls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import me.valour.milktea.*;

public class Bill{

	private ArrayList<User> users; 
	protected ArrayList<LineItem> lineItems;
	
	private double subtotal;

	double[][] priceMatrix;
	
	private double percentTip;
	private double percentTax;
	private double amountTax;
	private double amountTip;
	
	private boolean appliedTip = false;
	private boolean appliedTax = false;
	
	private BillChangesAgent changeAgent;
	
	public Bill(double tax, double tip, boolean splitEqually){
		super();
		
		users = new ArrayList<User>();
		lineItems = new ArrayList<LineItem>();
		
		subtotal = 0.0;
		percentTax = tax;
		percentTip = tip;
		amountTax = 0.0;
		amountTip = 0.0;
		if(splitEqually){
			priceMatrix = new double[Kitchen.maxBowls][1];
		} else {
			priceMatrix = new double[Kitchen.maxBowls][10];
		}
		createMatrix();
	}

	public double getTip(){
		return percentTip;
	}
	
	public double getTax(){
		return percentTax;
	}
	
	public double getTaxAmount(){
		if(appliedTax){
			return amountTax;
		} else {
			return 0.0;
		}
	}
	
	public double calculateTip(){
		if(appliedTip){
			amountTip = subtotal * percentTip;
			return amountTip;
		} else {
			return 0.0;
		}
	}
	
	public double calculateTax(){
		amountTax = Kitchen.roundSigFig(subtotal * percentTax, 2);
		return amountTax;
	}
	
	public void setTax(double tax){
		percentTax = tax;
		changeAgent.taxChanged(true);
	}
	
	public void setTip(double tip){
		percentTip = tip;
		changeAgent.tipChanged(true);
	}
	
	public void setTaxAmount(double amount){
		boolean rateChange = (amount/subtotal!=amountTax/subtotal);
		amountTax = amount;
		percentTax = amount/subtotal;
		changeAgent.taxChanged(rateChange);
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
	
	public void reapplyTax(){
		if(!appliedTax){
			return;
		}
		for(User u: users){
			u.setTax(u.getSubtotal()*percentTax);
		}
		changeAgent.taxChanged(false);
	}
	
	public void reapplyTip(){
		if(!appliedTip){
			return;
		}
		for(User u: users){
			u.setTip(u.getSubtotal()*percentTip);
		}
		changeAgent.tipChanged(false);
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
	
	public void createMatrix(){
		for(int i = 0; i<priceMatrix.length; i++){
			Arrays.fill(priceMatrix[i], 0.0);
		}
	}
	
	public void userAdd(User u){
		if(users.contains(u)){
			return;
		}
		users.add(u);
	}
	
	public void userRemove(User u){
		int userIndex = users.indexOf(u);
		if(userIndex<0 || userIndex>priceMatrix.length){
			return;
		}
		//go through list to redivide
		ArrayList<LineItem> toRemoveItems = new ArrayList<LineItem>();
		for(int j=0; j<priceMatrix[userIndex].length; j++){
			//redivide
			if(priceMatrix[userIndex][j]>0.0){
				LineItem li = lineItems.get(j);
				int usersCount = usersOfItemCount(j);
				if(usersCount>1){
					double newPrice = li.getPrice()/(usersCount-1);
					for(int i=0; i<priceMatrix.length; i++){
						if(i!=userIndex && priceMatrix[i][j]>0.0){
							priceMatrix[i][j] = newPrice;
						}
					}
				} else {
					toRemoveItems.add(li);
				}
				priceMatrix[userIndex][j] = 0.0;
			}
		}
		//move users beyond it up
		if(userIndex<(users.size()-1)){
			for(int i=(userIndex+1); i<users.size(); i++){
				for(int j=0; j<priceMatrix[i].length; j++){
					priceMatrix[i-1][j] = priceMatrix[i][j];
					priceMatrix[i][j] = 0.0;
				}
			}
		}
		users.remove(u);
		usersUpdateSubtotal();
		
		Iterator<LineItem> it = toRemoveItems.iterator();
		while(it.hasNext()){
			LineItem li = it.next();
			itemRemove(lineItems.indexOf(li));
		}
	}
	
	public void usersAddBatch(List<User> users){
		for(User id: users){
			userAdd(id);
		}
	}
	
	public int usersOfItemCount(LineItem li){
		return usersOfItemCount(lineItems.indexOf(li));
	}
	
	public List<User> usersOfItem(LineItem li){
		int ind = lineItems.indexOf(li);
		ArrayList<User> itemUsers = new ArrayList<User>();
		for(int i=0; i<priceMatrix.length; i++){
			if(priceMatrix[i][ind]>0.0){
				itemUsers.add(users.get(i));
			}
		}
		return itemUsers;
	}
	
	public int usersOfItemCount(int ind){
		int count = 0;
		for(int i=0; i<priceMatrix.length; i++){
			count += (priceMatrix[i][ind]>0.0) ? 1 : 0 ;
		}
		return count;
	}
	
	public double userSubtotal(User u){
		int i = users.indexOf(u);
		double amount = 0.0;
		for(int j=0; j<priceMatrix[i].length; j++){
			amount += priceMatrix[i][j];
		}
		if(u.getSubtotal()!=amount){
			u.setSubtotal(amount);
		}
		return amount;
	}
	
	public void usersUpdateSubtotal(){
		for(int i=0; i<users.size(); i++){
			User u = users.get(i);
			double amount = 0.0;
			for(int j=0; j<priceMatrix[i].length; j++){
				amount += priceMatrix[i][j];
			}
			u.setSubtotal(amount);
		}
	}
	
	public LineItem itemAdd(double price){
		LineItem li = new LineItem(price);

		int len = priceMatrix[0].length;
		if(lineItems.size()==len){
			double[][] newMatrix = new double[priceMatrix.length][len*2];
			for(int i=0; i<priceMatrix.length; i++){
				for(int j=0; j<len; j++){
					newMatrix[i][j] = priceMatrix[i][j];
				}
				for(int j=len; i<(len*2); j++){
					newMatrix[i][j] = 0.0;
				}
			}
			priceMatrix = newMatrix;
		}
		
		lineItems.add(li);
		subtotal += price;
		changeAgent.subtotalChanged();
		return li;
	}
	
	public void itemUpdate(LineItem li, double price){
		subtotal -= li.getPrice();
		subtotal += price;
		li.setPrice(price);
		
		int lineIndex = lineItems.indexOf(li);
		double pricePerUser = price / usersOfItemCount(lineIndex);
		for(int i=0; i<priceMatrix.length; i++){
			if(priceMatrix[i][lineIndex]>0.0){
				priceMatrix[i][lineIndex] = pricePerUser;
			}
		}
		usersUpdateSubtotal();
		changeAgent.subtotalChanged();
	}
	
	public void itemUpdate(LineItem li, List<User> newUsers){
		//clear existing users
		int lineIndex = lineItems.indexOf(li);
		for(int i=0; i<priceMatrix.length; i++){
				priceMatrix[i][lineIndex] = 0.0;
		}
		
		//set price for new users
		double pricePerUser = li.getPrice() / newUsers.size();
		for(User nu: newUsers){
			int userIndex = users.indexOf(nu);
			priceMatrix[userIndex][lineIndex] = pricePerUser;
		}
		
		usersUpdateSubtotal();
	}
	
	public void itemRemove(int lineIndex){
		LineItem li = lineItems.get(lineIndex);
		subtotal -= li.getPrice();
		
		for(int i=0; i<priceMatrix.length; i++){
			priceMatrix[i][lineIndex] = 0.0;
		}
		
		//move all columns left
		for(int j=(lineIndex+1); j<lineItems.size(); j++){
			for(int i=0; i<priceMatrix.length; i++){
				priceMatrix[i][j-1] = priceMatrix[i][j];
				priceMatrix[i][j] = 0.0;
			}
		}
		
		lineItems.remove(lineIndex);
		usersUpdateSubtotal();
		changeAgent.subtotalChanged();
		changeAgent.removeLineItemFromBill();
	}

	
	public void divideEqually(){
		if(lineItems.size()!=1 || users.isEmpty()){
			return;
		}
		LineItem li = lineItems.get(0);
		double pricePerUser = li.getPrice() / users.size();
		for(int i=0; i<users.size(); i++){
			priceMatrix[i][0] = pricePerUser;
		}
		usersUpdateSubtotal();
	}
	
	public void divideAmongst(LineItem li, List<User> us){
		if(us.isEmpty()){
			return;
		}
		int itemIndex = lineItems.indexOf(li);
		if(itemIndex<0){
			return;
		}
		double pricePerUser = li.getPrice() / us.size();
		for(User u: us){
			int userIndex = users.indexOf(u);
			priceMatrix[userIndex][itemIndex] = pricePerUser;
		}
		usersUpdateSubtotal();
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
		public void removeLineItemFromBill();
	}

}
