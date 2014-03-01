package me.valour.milktea;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class CrossTable<T,S,V> {

	protected Hashtable<Duo<T,S>,V> table;
	
	public CrossTable(){
		table = new Hashtable<Duo<T,S>, V>();
	}
	
	public V get(T row, S col, V defaultValue){
		V val = defaultValue;
		for(Duo<T,S> pair: table.keySet()){
			if(pair.equals(row, col)){
				val = table.get(pair);
				break;
			}
		}
		return val;
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
	
	public void addRow(T row, List<S> cols, V defaultValue){
		for(S c : cols){
		//	Duo<T,S> d = new Duo<T,S>(row,c);
		//	table.put(d, defaultValue);
			set(row,c, defaultValue);
		}
	}
	
	public void addCol(S col, List<T> rows, V defaultValue){
		for(T r : rows){
		//	Duo<T,S> d = new Duo<T,S>(r,col);
		//	table.put(d, defaultValue);
			set(r, col, defaultValue);
		}
	}
	
	public void rmRow(T row){
		Set<Duo<T,S>> keySet = table.keySet();
		for(Duo<T,S> pair: keySet){
			if(pair.hasRow(row)){
				table.remove(pair);
			}
		}
	}
	
	
	public void rmCol(S col){
		Set<Duo<T,S>> keySet = table.keySet();
		for(Duo<T,S> pair: keySet){
			if(pair.hasCol(col)){
				table.remove(pair);
			}
		}
	}
	
}
