package me.valour.milktea;

public class Duo<T,S> {
	
	private final T row;
	private final S col;
	
	public Duo(T x, S y) { 
	    this.row = x; 
	    this.col = y; 
	} 
	
	public boolean equals(T x, S y){
		return row.equals(x) && col.equals(y);
	}
	
	
}
