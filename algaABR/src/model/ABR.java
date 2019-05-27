package model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class ABR implements Serializable {
	
	
	private static final long serialVersionUID = -6757415158965732922L;

	/**
	 * A list of list of strings which stores execution step. 
	 * Information stored in the list are used to modify the view.
	 */
	public static ArrayList<ArrayList<String>> steps = new ArrayList<ArrayList<String>>();
	
	private Integer value;
	private Integer key;
	private ABR parent;
	private ABR right;
	private ABR left;
	
	private double cx, cy;

	/**
	 * Initializes an ABR object.
	 * @param k key value
	 * @param v value associated to the key
	 */
	public ABR(Integer k, Integer v) {
		key = k;
		value = v;
		parent = null;
		right = null;
		left = null;		
	}

    /**
     * Set the x coordinate of the position that the node will have within the view.    
     * @param x the x coordinate of the node 
     */
	public void setX(double x) {
		cx=x;
	}
	
	/**
     * Set the y coordinate of the position that the node will have within the view.    
     * @param y the y coordinate of the node
     */
	public void setY(double y) {
		cy=y;
	}
	
	/**
	 * Get the x coordinate of the position that the node has within the view.
	 * @return the x coordinate of the node
	 */
	public double getX() {
		return cx;		
	}
	
	/**
	 * Get the y coordinate of the position that the node has within the view.
	 * @return the y coordinate of the node
	 */
	public double getY() {
		return cy;		
	}
	
	/**
	 * Get the key value stored in the node.
	 * @return key stored in the node
	 */
	public Integer key() {
		return key;
	}
	
	/**
	 * Get the value stored in the node.
	 * @return the value stored in the node
	 */	
	public Integer value() {
		return value;
	}
	
	/**
	 * Get a reference to the parent of the node.
	 * @return reference to the parent of the node
	 */
	public ABR parent() {
		return parent;
	}
	
	/**
	 * Get a reference to the left child of the node.
	 * @return reference to the left child of the node
	 */
	public ABR left() {
		return left;
	}
	
	/**
	 * Get a reference to the right child of the node.
	 * @return reference to the right child of the node
	 */
	public ABR right() {
		return right;
	}
	
	/**
	 * Get the height of the Node in the tree.
	 * @param t the node of which to get the height
	 * @return a value which indicates the height of the node in the tree
	 */
	public int getNodeHeight(ABR t) {
		int height = 0;
		while(t.parent() != null) {
			t = t.parent();
			height++;
		}
		return height;
	}
	
	/**
	 * Links two nodes. p node becomes parent of u node.
	 * To decide if u node is the left or right child of p node this methods
	 * use a third parameter which is the key value of the new node it is compared
	 * with the key value of the parent node.
	 * O(1)
	 * @param p parent node
	 * @param u child node
	 * @param j key value of child node
	 */
	public void link(ABR p, ABR u, Integer j) {
		if(u != null) {
			u.parent = p; /* updates the parent of the new node to link */
		}
		if(p != null) {
			if(j < p.key()) { p.left = u;	} /* the new node becomes the left child */
			else { p.right = u; } /* the new node becomes the right child */
			}
		}
	
	
	/**
	 * Insert a node in the tree.
	 * Based on the key value searches the appropriate position where to insert the new node.
	 * If the key is already present in that position this methods updates the associated value.
	 * If the key is not already present in that position this methods initialize a new node and adds it.
	 * @param j key value of the new node
	 * @param v associated value of the new node
	 */
	public void insertNode(Integer j, Integer v){
		ABR p = null;
		ABR u = this;
		
		addStep(ABR.steps, "insertNode", 0, u.key, 666);
		addStep(ABR.steps, "insertNode", 1, u.key, 666);
		
		/* searches insertion position */
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
		if(u != null && u.key() == j) { /* the key is already present */
			addStep(ABR.steps, "insertNode", 10, u.key, 666);
			u.value = v;
		}else {
			addStep(ABR.steps, "insertNode", 11, 666, 666);
			ABR n = new ABR(j, v); /* the new node is created and linked */
			addStep(ABR.steps, "insertNode", 12, (u == null)? 666:u.key, 666);
			link(p,n,j);			
			addStep(ABR.steps, "insertNode", 13, 666, n.key);
		}
		addStep(ABR.steps, "insertNode", 13, j, 666);
	}
	
	/**
     * Adds an execution step to the execution steps list.
	 * Each step stores different information, needed to modify the view accordingly.
	 * This method adds a default value ("") to the step to indicates if is required or waived
	 * to highlight a node.
	 * This method takes a reference to an ABR which is serialized to base64 and added to the step.
	 * It's needed to store an intermediate state of the tree during the execution of removeNode method.
	 * @param steps a reference to the steps list 
	 * @param methodName the name of the method which is running
	 * @param codeLineToHighlight the number of the pseudocode line to highlight
	 * @param nodeValueToHighlight the key value of the node to highlight
	 * @param abr a reference to an ABR tree 
	 */
	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight, Integer nodeValueToHighlight, ABR abr) {
		// creates a step
		ArrayList<String> step = new ArrayList<String>();
		
		step.add(methodName);
		step.add(codeLineToHighlight.toString());
		
		if (nodeValueToHighlight >= -99 && nodeValueToHighlight <= 99)
			step.add(nodeValueToHighlight.toString());
		else
			step.add("");
			
		// serializes the tree to base64
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
		
		// adds the step at the end of the list
		ABR.steps.add(step);
	}

	/**
	 * Adds an execution step to the execution steps list.
	 * Each step stores different information, needed to modify the view accordingly.
	 * This method adds a default value ("") to the step to indicates if is required or waived
	 * to highlight/draw a node.
	 * @param steps a reference to the steps list 
	 * @param methodName the name of the method which is running
	 * @param codeLineToHighlight the number of the pseudocode line to highlight 
	 * @param nodeValueToHighlight the key value of the node to highlight 
	 * @param nodeValueToDraw the key value of the node to draw 
	 */
	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight, Integer nodeValueToHighlight, Integer nodeValueToDraw) {
		// creates a step
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
		
		// adds the step at the end of the execution steps list
		ABR.steps.add(step);
	}
	
	/**
	 * Given a reference to a node returns the root node of the tree which contains that node.
	 * @param t the node of which to return the root
	 * @return a reference to the root if t is not null, null otherwise
	 */
	public ABR getRoot(ABR t) {
		while (t != null && t.parent != null) {
			t = t.parent;
		}
		return t;
	}
	
	/**
	 * Given a key value searches for a node which stores that key and removes the node from the tree.
	 * Returns the root of the tree because it can changes if root node is removed.
	 * Stores execution steps.
	 * @param x the key value of the node to remove from the tree
	 * @return the root of the tree
	 */
	public ABR removeNode(Integer x) {
		ABR u = this.lookupNodeNoStep(x);
		addStep(ABR.steps, "removeNode", 0, u.key, 666);
		if (u != null) {
			addStep(ABR.steps, "removeNode", 1, u.key, 666);
			addStep(ABR.steps, "removeNode", 2, 666, 666);
			if (u.left != null && u.right != null) { /* the node has two subtrees */
				ABR s = u.right;
				addStep(ABR.steps, "removeNode", 3, (u.right == null)?666:u.right.key, 666);
				addStep(ABR.steps, "removeNode", 4, 666, 666);
				while (s.left != null) { /* searches the successornode */
					addStep(ABR.steps, "removeNode", 5, s.key, 666);
					s = s.left;
					addStep(ABR.steps, "removeNode", 4, s.key, 666);
				}
                /* the node to remove gets key and value from its successor node */
				u.key = s.key();     
				u.value = s.value();
				
				addStep(ABR.steps, "removeNode", 6, 666, (ABR) null);
				addStep(ABR.steps, "removeNode", 6, 666, this.getRoot(s));

				addStep(ABR.steps, "removeNode", 7, 666, 666);
                /* stores the key */
				x = s.key();
				addStep(ABR.steps, "removeNode", 8, 666, 666);
				u = s;
				addStep(ABR.steps, "removeNode", 9, 666, 666);
			}

			addStep(ABR.steps, "removeNode", 11, u.key, 666);
			/* the node to remove has one child */
			ABR t;
			addStep(ABR.steps, "removeNode", 12, 666, 666);
			if (u.left != null && u.right == null) {
				t = u.left;
				addStep(ABR.steps, "removeNode", 13, (t == null)?666:t.key, 666);
			} else {
				addStep(ABR.steps, "removeNode", 14, 666, 666);
				t = u.right;
				addStep(ABR.steps, "removeNode", 15, (t == null)?666:t.key, 666);
			}
			link(u.parent, t, x);
			addStep(ABR.steps, "removeNode", 17, 666, (ABR) null);
			if (t == null) {
				addStep(ABR.steps, "removeNode", 17, u.key, this.getRoot(u));
			} else {
				addStep(ABR.steps, "removeNode", 17, u.key, this.getRoot(t));
			}
			addStep(ABR.steps, "removeNode", 18, 666, 555);
            /* the node to remove is the root node */
			if (u.parent == null) { 
				addStep(ABR.steps, "removeNode", 19, (t == null)?666:t.key, 666);
				if (t != null) { /* node's child becomes root */
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
	
	/**
	 * Searches the node which contains the key passed as parameter.
	 * Stores execution steps.
	 * @param j the key value to look for
	 * @return a reference to the node which contains that key if found, null otherwise
	 */
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
		addStep(ABR.steps, "lookup", 5, t.key, 666);
		return t;
	}
	
	/**
	 * Searches the node which contains the key passed as parameter.
	 * This version of lookup doesn't store execution steps.
	 * @param j the key value to look for
	 * @return a reference to the node which contains that key if found, null otherwise
	 */
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
	
	/**
	 * Searches and returns the node which contains the minimum key value.
	 * Stores execution steps.
	 * @return a reference to the node which contains the minimum key value
	 */
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
	
	/**
	 * Searches and returns the node which contains the maximum key value.
 	 * Stores execution steps.
	 * @return a reference to the node which contains the maximum key value
	 */
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
	
	/**
	 * Based on key values comparisons searches the successor node of a specific node.
	 * Stores execution steps.
	 * @return a reference to the successor node if found, null otherwise
	 */
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
		addStep(ABR.steps, "successorNode", 11, (par == null)?666:par.key, 666);
		return par;
	}
	
	/**
	 * Based on key values comparisons searches the predecessor node of a specific node.
 	 * Stores execution steps.
	 * @return a reference to the predecessor node if found, null otherwise
	 */
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
		addStep(ABR.steps, "predecessorNode", 11, (par == null)?666:par.key, 666);
		return par;		
	}	

}
