package model;


public class ABR {
	
	private Integer value;
	private Integer key;
	private ABR parent;
	private ABR right;
	private ABR left;
	
	private double cx, cy;
	
	public ABR(Integer k, Integer v) {
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
	
	public Integer key() {
		return key;
	}
	
	public Integer value() {
		return value;
	}
	
	public ABR parent() {
		return parent;
	}
	
	public ABR left() {
		return left;
	}
	
	public ABR right() {
		return right;
	}
	
	public int getNodeHeight(ABR t) {
		int height = 0;
		while(t.parent() != null) {
			t = t.parent();
			height++;
		}
		return height;
	}
	
	public void link(ABR p, ABR u, Integer j) {
		if(u != null) {
			u.parent = p; /* aggiornamento padre del nodo che si vuole aggiungere */
		}
		if(p != null) {
			if(j < p.key()) { p.left = u;	} /* il nuovo nodo diventa figlio sinistro */
			else { p.right = u; } /* il nuovo nodo diventa figlio destro */
			}
		}
	
	
	
	public void insertNode(Integer j, Integer v){
		ABR p = null;
		ABR u = this;
		
		/* cerca posizione inserimento */
		while(u != null && u.key != j) {
			p = u;
			if(j < u.key()){ u = u.left(); }
			else { u = u.right(); }			
		}
		if(u != null && u.key() == j) { /* la chiave è già presente */
			u.value = v;
		}else {
			ABR n = new ABR(j, v); /* nodo creato e aggiunto */
			link(p,n,j);			
		}
	}
	
	
	public ABR removeNode(Integer x) {

		ABR u = this.lookupNode(x);
		if (u != null) {
			if (u.left != null && u.right != null) { /* il nodo ha due sottoalberi */
				ABR s = u.right;
				while (s.left != null) { /* si cerca il successore */
					s = s.left;
				}
                /* il nodo da eliminare prende i valori del successore */
				u.key = s.key();     
				u.value = s.value();
                /* si memorizza la chiave da parte */
				x = s.key();

				u = s;
			}

			/* il nodo da eliminare ha un solo figlio */
			ABR t;
			if (u.left != null && u.right == null) {
				t = u.left;
			} else {
				t = u.right;
			}/* se t non ha figli la link viene invocata con link(u.parent,null,x)*/

			link(u.parent, t, x);
            /* il nodo  da eliminare è root */
			if (u.parent == null) { 
				if (t != null) { /* suo figlio diventa root */
					t.parent = null; 
				}
				return t;
			}
		}
		return this;
	}
	
	public ABR lookupNode(Integer j){
		ABR t = this;
		while (t != null && t.key() != j) {
			if (j < t.key() ) { t = t.left;} 
			else { t = t.right; }			
		}
		return t;
	}
	
	public ABR min(){
		ABR t = this;
		while (t.left() != null) {
			t = t.left;
		}
		return t;
	}
	
	public ABR max(){
		ABR t = this;
		while (t.right() != null) {
			t = t.right;
		}
		return t;
	}
	
	public ABR successorNode(){
		ABR t = this;
		if(t == null) {
			return t;
		}
		if(t.right() != null) {
			return t.right().min();
		}
		ABR par = t.parent();
		while(par != null && t == par.right) {
			t = par;
			par = par.parent;
		}
		return par;
	}
	
	public ABR predecessorNode(){
		ABR t = this;
		if( t == null) {
			return t;
		}
		if(t.left != null) {
			return t.max();
		}
		ABR par = t.parent;
		while(par != null && t == par.left ) {
			t = par;
			par = t.parent;
		}
		return par;
		
	}	

}
