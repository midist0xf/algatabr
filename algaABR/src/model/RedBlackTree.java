package model;

public class RedBlackTree {
	
	private Node root;

	private enum Color {
		RED,
		BLACK;
	}
	
	private class Node {
		public Integer key;
		public Node leftChild, rightChild, parent;
		public Color color;

		public Node(Integer key, Node leftChild, Node rightChild, Node parent, Color color) {
			this.key = key;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			this.parent = parent;
			this.color = color;
		}
		
	}

	public RedBlackTree(int rootVal, int[] insertValues) {
		this.root = new Node(rootVal, null, null, null, Color.BLACK);
		
		for (int i : insertValues) {
			this.root = insertNode(this.root, i);
		}
	}

	/*
	 * il metodo inserisce, a partire dal nodo currentRoot, un nuovo
	 * nodo con chiave newKey
	 */
	public Node insertNode(Node relativeRoot, Integer newKey) {
		Node newNodeParent = null;
		Node u = relativeRoot;
		
		// u viene usato per cercare la posizione dove inserire
		while (u != null && u.key == newKey) {
			newNodeParent = u;
			u = (newKey < u.key) ? u.leftChild : u.rightChild;
		}
		
		// se alla fine della ricerca troviamo gia un nodo con chiave newKey
		if (u != null && u.key == newKey) {
			return relativeRoot;
		} else {
			// Altrimenti dobbiamo inserire una foglia, poi bilanciamo tutto
			Node newNode = new Node(newKey, null, null, null, Color.RED);
			link(newNodeParent, newNode);
			balanceInsert(newNode);
			
			// andiamo alla ricerca della root
			while (newNode.parent != null) {
				newNode = newNode.parent;
			}
			
			return newNode;
		}
		
	}

	private void balanceInsert(Node node) {
		node.color = Color.RED;
		
		while (node != null) {
			Node nodeParent = node.parent;
			Node nodeGParent = (nodeParent.parent == null) ? null : nodeParent.parent;
			Node nodeUncle = (nodeGParent.parent == null) ? null : ((nodeGParent.leftChild == nodeParent)? nodeGParent.rightChild : nodeGParent.leftChild);
			
			if (nodeParent == null) {
				node.color = Color.BLACK;
				node = null;
			} else if (nodeParent.color == Color.BLACK) {
				node = null;
			} else if (nodeUncle.color == Color.RED) {
				nodeParent.color = Color.BLACK;
				nodeUncle.color = Color.BLACK;
				
				node.color = Color.RED;
				node = nodeGParent;
			} else {
				if (node == nodeParent.rightChild && nodeParent == nodeGParent.leftChild) {
					rotateLeft(nodeParent);
					node = nodeParent;
				} else if (node == nodeParent.leftChild && nodeParent == nodeGParent.rightChild) {
					rotateRight(nodeParent);
					node = nodeParent;
				} else {
					if (node == nodeParent.leftChild && nodeParent == nodeGParent.leftChild) {
						rotateRight(nodeGParent);
					} else if (node == nodeParent.rightChild && nodeParent == nodeGParent.rightChild) {
						rotateLeft(nodeGParent);
					}
					
					nodeParent.color = Color.BLACK;
					nodeGParent.color = Color.RED;
					node = null;
				}
			}

		}
	}


	private Node rotateRight(Node x) {
		Node y = x.leftChild;
		Node nodeParent = x.parent;
		
		x.leftChild = y.rightChild;
		
		if (y.rightChild != null) {
			y.rightChild.parent = x;
		}
		
		y.rightChild = x;
		x.parent = y;
		y.parent = nodeParent;
		
		if (nodeParent != null) {
			if (nodeParent.rightChild == x) {
				nodeParent.rightChild = y;
			} else {
				nodeParent.leftChild = y;
			}
		}
		
		return y;
	}

	private Node rotateLeft(Node x) {
		Node y = x.rightChild;
		Node nodeParent = x.parent;
		
		x.rightChild = y.leftChild;
		
		if (y.leftChild != null) {
			y.leftChild.parent = x;
		}
		
		y.leftChild = x;
		x.parent = y;
		y.parent = nodeParent;
		
		if (nodeParent != null) {
			if (nodeParent.leftChild == x) {
				nodeParent.leftChild = y;
			} else {
				nodeParent.rightChild = y;
			}
		}
		
		return y;
	}

	private void link(Node newNodeParent, Node newNode) {
		if (newNode != null) {
			newNode.parent = newNodeParent;
		}
		
		if (newNodeParent != null) {
			if (newNode.key < newNodeParent.key ) {
				newNodeParent.leftChild = newNode;
			} else {
				newNodeParent.rightChild = newNode;
			}
		}
	}
	
	private Node removeNode(Node relativeRoot, int keyToRemove) {
		Node u = lookupNode(relativeRoot, keyToRemove);
		 if (u != null) {
			 if (u.leftChild != null && u.rightChild != null) {
				 Node s = u.rightChild;
				 while (s.leftChild != null) {
					 s = s.leftChild;
				 }
				 u.key = s.key;
				 keyToRemove = s.key;
				 u = s;
			 }
			 
			 Node t = new Node(null, null, null, null, null);
			  
			 if (u.leftChild != null && u.rightChild == null) {
				 t = u.leftChild;
			 } else {
				 t = u.rightChild;
			 }
			 
			 link(u.parent, t);
			 
			 if (u.color == Color.BLACK) {
				 balanceDelete(relativeRoot, t);
			 }
			 
			 if (u.parent == null) {
				 relativeRoot = t;
			 }
		 }
		 
		 while (relativeRoot.parent != null) {
			 relativeRoot = relativeRoot.parent;
		 }
		 
		 return relativeRoot;
	}

	private void balanceDelete(Node T, Node t) {
		while (t != T && t.color == Color.BLACK) {
			Node p = t.parent;
			
			if (t == p.leftChild) {
				Node f = p.rightChild;
				Node ns = f.leftChild;
				Node nd = f.rightChild;
				
				if (f.color == Color.RED) {
					p.color = Color.RED;
					f.color = Color.BLACK;
					rotateLeft(p);
				} else {
					if (ns.color == nd.color && nd.color == Color.BLACK) {
						f.color = Color.RED;
						t = p;
					} else if (ns.color == Color.RED && nd.color == Color.BLACK) {
						ns.color = Color.BLACK;
						f.color = Color.RED;
						rotateRight(f);
					} else if (nd.color == Color.RED) {
						f.color = p.color;
						p.color = Color.BLACK;
						nd.color = Color.BLACK;
						rotateLeft(p);
						t = T;
					}
				}
			} else {
				Node f = p.leftChild;
				Node ns = f.rightChild;
				Node nd = f.leftChild;
				
				if (f.color == Color.RED) {
					p.color = Color.RED;
					f.color = Color.BLACK;
					rotateRight(p);
				} else {
					if (ns.color == nd.color && nd.color == Color.BLACK) {
						f.color = Color.RED;
						t = p;
					} else if (ns.color == Color.RED && nd.color == Color.BLACK) {
						ns.color = Color.BLACK;
						f.color = Color.RED;
						rotateLeft(f);
					} else if (nd.color == Color.RED) {
						f.color = p.color;
						p.color = Color.BLACK;
						nd.color = Color.BLACK;
						rotateRight(p);
						t = T;
					}
				}
				
			}
		}

		if (t != null) {
			t.color = Color.BLACK;
		}
	}

	private Node lookupNode(Node relativeRoot, int keySearch) {
		while (relativeRoot != null && relativeRoot.key != keySearch) {
			relativeRoot = (keySearch < relativeRoot.key) ? relativeRoot.leftChild : relativeRoot.rightChild;
		}
		return relativeRoot;
	}
	
}
