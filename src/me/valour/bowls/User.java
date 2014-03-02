package me.valour.bowls;

public class User {

	public final int bowlViewId;
	private double subtotal;
	private double tax;
	private double tip;
	
//	private ArrayList<LineItem> items;
	
	public User(int id){
		bowlViewId = id;
		subtotal = 0.0;
		tax = 0.0;
		tip = 0.0;
	//	items = new ArrayList<LineItem>();
	}

	/**
	 * @return the subtotal
	 */
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal the subtotal to set
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	
	public double plusSubtotal(double amount){
		this.subtotal += amount;
		return subtotal;
	}
	
	public double subtractSubtotal(double amount){
		this.subtotal -= amount;
		return subtotal;
	}

	/**
	 * @return the tax
	 */
	public double getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}
	
	public void applyTax(double percent){
		this.tax = subtotal*percent;
	}

	/**
	 * @return the tip
	 */
	public double getTip() {
		return tip;
	}

	/**
	 * @param tip the tip to set
	 */
	public void setTip(double tip) {
		this.tip = tip;
	}
	
	public void applyTip(double percent){
		this.tip = subtotal*percent;
	}
	
	public double getTotal(){
		return subtotal + tax + tip;
	}
	
/*	public boolean had(LineItem item){
		return items.contains(item);
	}
	
	public boolean hadItems(){
		return !items.isEmpty();
	} */
	
	
/*	public void add(LineItem item){
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
 */
}
