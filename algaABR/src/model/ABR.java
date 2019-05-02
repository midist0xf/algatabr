package model;

import java.util.ArrayList;
import java.util.List;

public class ABR {
	
	public static ArrayList<ArrayList<String>> steps = new ArrayList<ArrayList<String>>();
	
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
		
		addStep(ABR.steps, "insertNode", 0, u.key, 666);
		addStep(ABR.steps, "insertNode", 1, u.key, 666);
		
		/* cerca posizione inserimento */
		while(u != null && u.key != j) {
			addStep(ABR.steps, "insertNode", 2, u.key, 666);
			addStep(ABR.steps, "insertNode", 3, u.key, 666);
			p = u;
			addStep(ABR.steps, "insertNode", 4, u.key, 666);
			if(j < u.key()) {
				u = u.left(); 
				addStep(ABR.steps, "insertNode", 5, (u == null)? 666:u.key, 666);
			} else { 
				addStep(ABR.steps, "insertNode", 6, u.key, 666);
				u = u.right(); 
				addStep(ABR.steps, "insertNode", 7, (u == null)? 666:u.key, 666);
			}			
		}

		addStep(ABR.steps, "insertNode", 9, 666, 666);
		if(u != null && u.key() == j) { /* la chiave è già presente */
			addStep(ABR.steps, "insertNode", 10, u.key, 666);
			u.value = v;
		}else {
			addStep(ABR.steps, "insertNode", 11, 666, 666);
			ABR n = new ABR(j, v); /* nodo creato e aggiunto */
			addStep(ABR.steps, "insertNode", 12, (u == null)? 666:u.key, 666);
			link(p,n,j);			
			addStep(ABR.steps, "insertNode", 13, 666, n.key);
		}
	}
	
	
	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight, Integer nodeValueToHighlight, Integer nodeValueToDraw) {
		// creo uno step da aggiungere alla lista di tutti gli steps
		ArrayList<String> step = new ArrayList<String>();
		
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
		ABR.steps.add(step);
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
			}/* se t non ha figli la link viene invocata con 
(u.parent,null,x)*/

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
			addStep(ABR.steps, "lookupNode", 0, t.key, 666);
			addStep(ABR.steps, "lookupNode", 1, 666, 666);
			if (j < t.key() ) {
				t = t.left;
				addStep(ABR.steps, "lookupNode", 2, t.key, 666);
			} 
			else {
				t = t.right;
				addStep(ABR.steps, "lookupNode", 4, t.key, 666);
            }			
		}
		addStep(ABR.steps, "lookupNode", 5, 666, 666);
		return t;
	}
	
	public ABR lookupNodeNoStep(Integer j) {
		ABR t = this;
		while (t != null && t.key() != j) {
			if (j < t.key() ) {
				t = t.left;
			} 
			else {
				t = t.right;
            }			
		}
		return t;		
	}
	
	public ABR min(){
		ABR t = this;
		while (t.left() != null) {
			addStep(ABR.steps, "min", 0, t.key, 666);
			t = t.left;		
			addStep(ABR.steps, "min", 1, t.key, 666);
		}
		addStep(ABR.steps, "min", 2,t.key, 666);
		return t;		
	}
	
	public ABR max(){
		ABR t = this;
		while (t.right() != null) {
			addStep(ABR.steps, "max", 0, t.key, 666);
			t = t.right;
			addStep(ABR.steps, "max", 1, t.key, 666);
		}
		addStep(ABR.steps, "max", 2, t.key, 666);
		return t;
	}
	
	public ABR successorNode(){
		ABR t = this;
		addStep(ABR.steps, "successorNode", 0, t.key, 666);
		if(t.right() != null) {
			addStep(ABR.steps, "successorNode", 1,666, 666);
			addStep(ABR.steps, "successorNode", 2, 666, 666);
			return t.right().min();
		}
		ABR par = t.parent();
		addStep(ABR.steps, "successorNode", 4, 666, 666);
		addStep(ABR.steps, "successorNode", 5, 666, 666);
		while(par != null && t == par.right) {
			addStep(ABR.steps, "successorNode", 5, 666, 666);
			t = par;
			addStep(ABR.steps, "successorNode", 6, 666, 666);
			par = par.parent;
			addStep(ABR.steps, "successorNode", 7, (par == null)?666:par.key, 666);
		}
		addStep(ABR.steps, "successorNode", 8, (par == null)?666:par.key, 666);
		return par;
	}
	
	public ABR predecessorNode(){
		ABR t = this;
		addStep(ABR.steps, "predecessorNode", 0, t.key, 666);
		addStep(ABR.steps, "predecessorNode", 1, 666, 666);
		if(t.left != null) {
			addStep(ABR.steps, "predecessorNode", 2, 666, 666);
			return t.left().max();
		}
		ABR par = t.parent;
		addStep(ABR.steps, "predecessorNode", 4, 666, 666);
		addStep(ABR.steps, "predecessorNode", 5, 666, 666);
		while(par != null && t == par.left ) {
			addStep(ABR.steps, "predecessorNode", 5, 666, 666);
			t = par;
			addStep(ABR.steps, "predecessorNode", 6, 666, 666);
			par = t.parent;
			addStep(ABR.steps, "predecessorNode", 7,(par == null)?666:par.key, 666);

		}
		addStep(ABR.steps, "predecessorNode", 9,(par == null)?666:par.key, 666);
		return par;		
	}	

}
