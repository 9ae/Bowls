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
	
	public boolean hasRow(T x){
		return row.equals(x);
	}
	
	public boolean hasCol(S y){
		return col.equals(y);
	}

	/**
	 * @return the row
	 */
	public T getRow() {
		return row;
	}

	/**
	 * @return the col
	 */
	public S getCol() {
		return col;
	}
	
	
}
