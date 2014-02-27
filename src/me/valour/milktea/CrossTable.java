package me.valour.milktea;

import java.util.Hashtable;

public class CrossTable<T,S,V> {

	private Hashtable<Duo<T,S>,V> table;
	
	public CrossTable(){
		table = new Hashtable<Duo<T,S>, V>();
	}
	
	public void set(T row, S col, V val){
		Duo<T,S> d = null;
		for(Duo<T,S> pair: table.keySet()){
			if(pair.equals(row, col)){
				d = pair;
				break;
			}
		}
		if(d==null){
			d = new Duo<T,S>(row, col);
		} else {
			table.remove(d);
		}
		table.put(d,val);
	}
	
	public V remove(T row, S col){
		Duo<T,S> d = null;
		for(Duo<T,S> pair: table.keySet()){
			if(pair.equals(row, col)){
				d = pair;
				break;
			}
		}
		
		if(d!=null){
			return table.remove(d);
		} else {
			return null;
		}
	}
	
}
