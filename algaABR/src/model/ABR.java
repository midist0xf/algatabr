package model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ABR implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6757415158965732922L;

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
	
	
	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight, Integer nodeValueToHighlight, ABR abr) {
		// creo uno step da aggiungere alla lista di tutti gli steps
		ArrayList<String> step = new ArrayList<String>();
		
		step.add(methodName);
		step.add(codeLineToHighlight.toString());
		
		if (nodeValueToHighlight >= -99 && nodeValueToHighlight <= 99)
			step.add(nodeValueToHighlight.toString());
		else
			step.add("");
			
		// serializza l' abr in stringa
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			
			so.writeObject(abr);
			so.flush();
			step.add(Base64.getEncoder().encodeToString(bo.toByteArray()));
			so.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// aggiungo lo step in fondo alla lista
		ABR.steps.add(step);
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
	
	public ABR getRoot(ABR t) {
		while (t != null && t.parent != null) {
			t = t.parent;
		}
		return t;
	}
	
	public ABR removeNode(Integer x) {
		ABR u = this.lookupNodeNoStep(x);
		addStep(ABR.steps, "removeNode", 0, u.key, 666);
		if (u != null) {
			addStep(ABR.steps, "removeNode", 1, u.key, 666);
			addStep(ABR.steps, "removeNode", 2, 666, 666);
			if (u.left != null && u.right != null) { /* il nodo ha due sottoalberi */
				ABR s = u.right;
				addStep(ABR.steps, "removeNode", 3, (u.right == null)?666:u.right.key, 666);
				addStep(ABR.steps, "removeNode", 4, 666, 666);
				while (s.left != null) { /* si cerca il successore */
					addStep(ABR.steps, "removeNode", 5, s.key, 666);
					s = s.left;
					addStep(ABR.steps, "removeNode", 4, s.key, 666);
				}
                /* il nodo da eliminare prende i valori del successore */
				u.key = s.key();     
				u.value = s.value();
				
				addStep(ABR.steps, "removeNode", 6, 666, (ABR) null);
				addStep(ABR.steps, "removeNode", 6, 666, this.getRoot(s));

				addStep(ABR.steps, "removeNode", 7, 666, 666);
                /* si memorizza la chiave da parte */
				x = s.key();
				addStep(ABR.steps, "removeNode", 8, 666, 666);
				u = s;
				addStep(ABR.steps, "removeNode", 9, 666, 666);
			}

			addStep(ABR.steps, "removeNode", 11, u.key, 666);
			/* il nodo da eliminare ha un solo figlio */
			ABR t;
			addStep(ABR.steps, "removeNode", 12, 666, 666);
			if (u.left != null && u.right == null) {
				t = u.left;
				addStep(ABR.steps, "removeNode", 13, (t == null)?666:t.key, 666);
			} else {
				addStep(ABR.steps, "removeNode", 14, 666, 666);
				t = u.right;
				addStep(ABR.steps, "removeNode", 15, (t == null)?666:t.key, 666);
			}/* se t non ha figli la link viene invocata con 
(u.parent,null,x)*/

			link(u.parent, t, x);
			addStep(ABR.steps, "removeNode", 17, 666, (ABR) null);
			addStep(ABR.steps, "removeNode", 17, u.key, this.getRoot(t));
			addStep(ABR.steps, "removeNode", 18, 666, 555);
            /* il nodo  da eliminare è root */
			if (u.parent == null) { 
				addStep(ABR.steps, "removeNode", 19, (t == null)?666:t.key, 666);
				if (t != null) { /* suo figlio diventa root */
					t.parent = null; 
					addStep(ABR.steps, "removeNode", 20, 666, (ABR) null);
					addStep(ABR.steps, "removeNode", 20, t.key, this.getRoot(t));
				}
				addStep(ABR.steps, "removeNode", 21, 666, (ABR) null);
				addStep(ABR.steps, "removeNode", 21, 666, 69);
				return t;
			}
		}
		addStep(ABR.steps, "removeNode", 22, 666, (ABR) null);
		addStep(ABR.steps, "removeNode", 22, 666, 69);
		return this;
	}
	
	public ABR lookupNode(Integer j){
		ABR t = this;
		addStep(ABR.steps, "lookup", 0, t.key, 666);
		while (t != null && t.key() != j) {
			addStep(ABR.steps, "lookup", 1, 666, 666);
			if (j < t.key() ) {
				t = t.left;
				addStep(ABR.steps, "lookup", 2, t.key, 666);
			} 
			else {
				t = t.right;
				addStep(ABR.steps, "lookup", 4, t.key, 666);
            }			
		}
		addStep(ABR.steps, "lookup", 5, 666, 666);
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
		addStep(ABR.steps, "min", 0, t.key, 666);
		while (t.left() != null) {
			t = t.left;		
			addStep(ABR.steps, "min", 1, t.key, 666);
			addStep(ABR.steps, "min", 0, t.key, 666);
		}
		addStep(ABR.steps, "min", 2,t.key, 666);
		return t;		
	}
	
	public ABR max(){
		ABR t = this;
		addStep(ABR.steps, "max", 0, t.key, 666);
		while (t.right() != null) {
			t = t.right;
			addStep(ABR.steps, "max", 1, t.key, 666);
			addStep(ABR.steps, "max", 0, t.key, 666);
		}
		addStep(ABR.steps, "max", 2, t.key, 666);
		return t;
	}
	
	public ABR successorNode(){
		ABR t = this;
		addStep(ABR.steps, "successorNode", 0, t.key, 666);
		if (t == null) {
			addStep(ABR.steps, "successorNode", 1, t.key, 666);
			return t;
		}

		addStep(ABR.steps, "successorNode", 3,666, 666);
		if(t.right() != null) {
			addStep(ABR.steps, "successorNode", 4, 666, 666);
			return t.right().min();
		}
		addStep(ABR.steps, "successorNode", 6, (t.parent == null)?666:t.parent.key, 666);
		ABR par = t.parent();
		addStep(ABR.steps, "successorNode", 7, 666, 666);
		while(par != null && t == par.right) {
			t = par;
			addStep(ABR.steps, "successorNode", 8, 666, 666);
			par = par.parent;
			addStep(ABR.steps, "successorNode", 9, (par == null)?666:par.key, 666);
			
			addStep(ABR.steps, "successorNode", 7, 666, 666);
		}
		addStep(ABR.steps, "successorNode", 11, 666, 666);
		return par;
	}
	
	public ABR predecessorNode(){
		ABR t = this;
		addStep(ABR.steps, "predecessorNode", 0, t.key, 666);
		if (t == null) {
			addStep(ABR.steps, "predecessorNode", 1, t.key, 666);
			return t;
		}

		addStep(ABR.steps, "predecessorNode", 3, 666, 666);
		if(t.left != null) {
			addStep(ABR.steps, "predecessorNode", 4, 666, 666);
			return t.left().max();
		}
		addStep(ABR.steps, "predecessorNode", 6, (t.parent == null)?666:t.parent.key, 666);
		ABR par = t.parent;
		addStep(ABR.steps, "predecessorNode", 7, 666, 666);
		while(par != null && t == par.left ) {
			t = par;
			addStep(ABR.steps, "predecessorNode", 8, 666, 666);
			par = t.parent;
			addStep(ABR.steps, "predecessorNode", 9,(par == null)?666:par.key, 666);

			addStep(ABR.steps, "predecessorNode", 7, 666, 666);
		}
		addStep(ABR.steps, "predecessorNode", 11, 666, 666);
		return par;		
	}	

}
