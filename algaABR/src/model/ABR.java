package model;

import java.util.ArrayList;
import java.util.List;

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
	
	
	
	public void insertNode(Integer j, Integer v, List<List<String>> steps){
		ABR p = null;
		ABR u = this;
		
		//addStep(steps, "insertNode", 0, this.value, 666);
		
		/* cerca posizione inserimento */
		while(u != null && u.key != j) {
			p = u;
			if(j < u.key()) {
				u = u.left(); 
				addStep(steps, "insertNode", 5, u.value, 666);
			} else { 
				u = u.right(); 
				addStep(steps, "insertNode", 7, u.value, 666);
			}			
		}
		if(u != null && u.key() == j) { /* la chiave è già presente */
			addStep(steps, "insertNode", 10, u.value, 666);
			u.value = v;
		}else {
			ABR n = new ABR(j, v); /* nodo creato e aggiunto */
			addStep(steps, "insertNode", 12, u.value, 666);
			link(p,n,j);			
			addStep(steps, "insertNode", 13, 666, n.value);
		}
	}
	
	
	private void addStep(List<List<String>> steps, String methodName, Integer codeLineToHighlight, Integer nodeValueToHighlight, Integer nodeValueToDraw) {
		// creo uno step da aggiungere alla lista di tutti gli steps
		List<String> step = new ArrayList<String>();
		
		step.add(methodName);
		step.add(codeLineToHighlight.toString());
		
		if (nodeValueToHighlight >= -99 && nodeValueToHighlight <= 99)
			step.add(nodeValueToHighlight.toString());
		else
			step.add("");
			
		if (nodeValueToDraw >= -99 && nodeValueToDraw <= 99)
			step.add(nodeValueToDraw.toString());
		else
			step.add("");
		
		// aggiungo lo step in fondo alla lista
		steps.add(step);
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

		if(t.left != null) {
			return t.left().max();
		}
		ABR par = t.parent;
		while(par != null && t == par.left ) {
			t = par;
			par = t.parent;
		}
		return par;		
	}	

}
