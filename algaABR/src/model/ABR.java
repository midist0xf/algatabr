package model;


public class ABR<T extends Comparable<? super T>> {
	
	private T value;
	private T key;
	private ABR<T> parent;
	private ABR<T> right;
	private ABR<T> left;
	
	private double cx, cy;
	
	public ABR(T k, T v) {
		key = k;
		value = v;
		parent = null;
		right = null;
		left = null;
		
	}

	public void setX(double x) {
		cx=x;
	}
	public void setY(double y) {
		cy=y;
	}
	
	public double getX() {
		return cx;		
	}
	
	public double getY() {
		return cy;		
	}
	
	public T key() {
		return key;
	}
	
	public T value() {
		return value;
	}
	
	public ABR<T> parent() {
		return parent;
	}
	
	public ABR<T> left() {
		return left;
	}
	
	public ABR<T> right() {
		return right;
	}
	
	public int getNodeHeight(ABR<T> t) {
		int height = 0;
		while(t.parent() != null) {
			t = t.parent();
			height++;
		}
		return height;
	}
	
	public void link(ABR<T> p, ABR<T> u, T j) {
		if(u != null) {
			u.parent = p;
		}
		if(p != null) {
			if(j.compareTo(p.key()) < 0) { p.left = u;	} 
			else { p.right = u; }
			}
		}
	
	
	
	public void insertNode(T j, T v){
		ABR<T> p = null;
		ABR<T> u = this;
		
		/* cerca posizione inserimento */
		while(u != null && u.key != j) {
			p = u;
			if(j.compareTo(u.key()) < 0){ u = u.left(); }
			else { u = u.right(); }			
		}
		if(u != null && u.key() == j) { /* la chiave è già presente */
			u.value = v;
		}else {
			ABR<T> n = new ABR<T>(j, v);
			link(p,n,j);			
		}
	}
	
	/* TODO: implementare la remove node */
	
	public ABR<T> lookupNode(T j){
		ABR<T> t = this;
		while (t != null && t.key() != j) {
			if (j.compareTo(t.key()) < 0 ) { t = t.left;} 
			else { t = t.right; }			
		}
		return t;
	}
	
	public ABR<T> min(){
		ABR<T> t = this;
		while (t.left() != null) {
			t = t.left;
		}
		return t;
	}
	
	public ABR<T> max(){
		ABR<T> t = this;
		while (t.right() != null) {
			t = t.right;
		}
		return t;
	}
	
	public ABR<T> successorNode(){
		ABR<T> t = this;
		if(t == null) {
			return t;
		}
		if(t.right() != null) {
			return t.right().min();
		}
		ABR<T> par = t.parent();
		while(par != null && t == par.right) {
			t = par;
			par = par.parent;
		}
		return par;
	}
	
	public ABR<T> predecessorNode(){
		ABR<T> t = this;
		if( t == null) {
			return t;
		}
		if(t.left != null) {
			return t.max();
		}
		ABR<T> par = t.parent;
		while(par != null && t == par.left ) {
			t = par;
			par = t.parent;
		}
		return par;
		
	}	

}
