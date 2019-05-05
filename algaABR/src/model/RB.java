package model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RB implements Serializable {

	private static final long serialVersionUID = -6757415158965732922L;

	public static ArrayList<ArrayList<String>> steps = new ArrayList<ArrayList<String>>();

	private Integer value;
	private Integer key;
	private RB parent;
	private RB right;
	private RB left;
	private Color color;

	private double cx, cy;

	public enum Color {
		RED, BLACK;
	}

	public RB(Integer k, Integer v, Color c) {
		key = k;
		value = v;
		parent = null;
		right = null;
		left = null;
		color = c;
	}
	
	public Color getColor() {
		return color;
	}

	public void setX(double x) {
		cx = x;
	}

	public void setY(double y) {
		cy = y;
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

	public RB parent() {
		return parent;
	}

	public RB left() {
		return left;
	}

	public RB right() {
		return right;
	}

	public int getNodeHeight(RB t) {
		int height = 0;
		while (t.parent() != null) {
			t = t.parent();
			height++;
		}
		return height;
	}

	public void link(RB p, RB u, Integer j) {
		if (u != null) {
			u.parent = p; /* aggiornamento padre del nodo che si vuole aggiungere */
		}
		if (p != null) {
			if (j < p.key()) {
				p.left = u;
			} /* il nuovo nodo diventa figlio sinistro */
			else {
				p.right = u;
			} /* il nuovo nodo diventa figlio destro */
		}
	}

	public void rotateLeft(RB x) {
		RB y = x.right;
		RB p = x.parent;
		x.right = y.left;

		if (y.left != null) {
			y.left.parent = x;
		}

		y.left = x;
		x.parent = y;
		y.parent = p;

		if (p != null) {
			if (p.left == x) {
				p.left = y;
			} else {
				p.right = y;
			}
		}
	}

	public void rotateRight(RB x) {
		RB y = x.left;
		RB p = x.parent;
		x.left = y.right;

		if (y.right != null) {
			y.right.parent = x;
		}

		y.right = x;
		x.parent = y;
		y.parent = p;

		if (p != null) {
			if (p.left == x) {
				p.left = y;
			} else {
				p.right = y;
			}
		}
	}

	private void balanceInsert(RB t) {
		t.color = Color.RED;
		while (t != null) {
			RB p = t.parent;
			RB n = (p != null) ? p.parent : null;
			RB z = (n == null) ? null : ((n.left == p) ? n.right : n.left);
			if (p == null) // case 1
			{
				t.color = Color.BLACK;
				t = null;
			} else if (p.color == Color.BLACK) // case 2
			{
				t = null;
			} else if (z != null && z.color == Color.RED) // case 3
			{
				p.color = z.color = Color.BLACK;
				n.color = Color.RED;
				t = n;
			} else {
				if (t == p.right && p == n.left) // case 4.a
				{
					rotateLeft(p);
					t = p;
				} else if (t == p.left && p == n.right) // case 4.b
				{
					rotateRight(p);
					t = p;
				} else {
					if (t == p.left && p == n.left) // case 5.a
						rotateRight(n);
					else if (t == p.right && p == n.right)
						rotateLeft(n);
					p.color = Color.BLACK;
					n.color = Color.RED;
					t = null;
				}
			}
		}
	}

	public void insertNode(Integer j, Integer v) {
		RB p = null;
		RB u = this;

		addStep(RB.steps, "insertNode", 0, u.key, 666);
		addStep(RB.steps, "insertNode", 1, u.key, 666);

		/* cerca posizione inserimento */
		while (u != null && u.key != j) {
			addStep(RB.steps, "insertNode", 2, u.key, 666);
			addStep(RB.steps, "insertNode", 3, u.key, 666);
			p = u;
			addStep(RB.steps, "insertNode", 4, u.key, 666);
			if (j < u.key()) {
				u = u.left();
				addStep(RB.steps, "insertNode", 5, (u == null) ? 666 : u.key, 666);
			} else {
				addStep(RB.steps, "insertNode", 6, u.key, 666);
				u = u.right();
				addStep(RB.steps, "insertNode", 7, (u == null) ? 666 : u.key, 666);
			}
		}

		addStep(RB.steps, "insertNode", 9, 666, 666);
		if (u != null && u.key() == j) { /* la chiave è già presente */
			addStep(RB.steps, "insertNode", 10, u.key, 666);
			u.value = v;
		} else {
			addStep(RB.steps, "insertNode", 11, 666, 666);
			RB n = new RB(j, v, Color.BLACK); /* nodo creato e aggiunto */
			addStep(RB.steps, "insertNode", 12, (u == null) ? 666 : u.key, 666);
			link(p, n, j);
			addStep(RB.steps, "insertNode", 13, 666, n.key);
			balanceInsert(n);
			addStep(RB.steps, "insertNode", 14, 666, 666);
			addStep(RB.steps, "insertNode", 14, 666, (RB) null);
			addStep(RB.steps, "insertNode", 14, 666, n.getRoot(n));
		}
	}

	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight,
			Integer nodeValueToHighlight, RB abr) {
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
		RB.steps.add(step);
	}

	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight,
			Integer nodeValueToHighlight, Integer nodeValueToDraw) {
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
		RB.steps.add(step);
	}

	public RB getRoot(RB t) {
		while (t != null && t.parent != null) {
			t = t.parent;
		}
		return t;
	}

	public RB removeNode(Integer x) {
		RB u = this.lookupNodeNoStep(x);
		addStep(RB.steps, "removeNode", 0, u.key, 666);
		if (u != null) {
			addStep(RB.steps, "removeNode", 1, u.key, 666);
			addStep(RB.steps, "removeNode", 2, 666, 666);
			if (u.left != null && u.right != null) { /* il nodo ha due sottoalberi */
				RB s = u.right;
				addStep(RB.steps, "removeNode", 3, (u.right == null) ? 666 : u.right.key, 666);
				addStep(RB.steps, "removeNode", 4, 666, 666);
				while (s.left != null) { /* si cerca il successore */
					addStep(RB.steps, "removeNode", 5, s.key, 666);
					s = s.left;
					addStep(RB.steps, "removeNode", 4, s.key, 666);
				}
				/* il nodo da eliminare prende i valori del successore */
				u.key = s.key();
				u.value = s.value();

				addStep(RB.steps, "removeNode", 6, 666, (RB) null);
				addStep(RB.steps, "removeNode", 6, 666, this.getRoot(s));

				addStep(RB.steps, "removeNode", 7, 666, 666);
				/* si memorizza la chiave da parte */
				x = s.key();
				addStep(RB.steps, "removeNode", 8, 666, 666);
				u = s;
				addStep(RB.steps, "removeNode", 9, 666, 666);
			}

			addStep(RB.steps, "removeNode", 11, u.key, 666);
			/* il nodo da eliminare ha un solo figlio */
			RB t;
			addStep(RB.steps, "removeNode", 12, 666, 666);
			if (u.left != null && u.right == null) {
				t = u.left;
				addStep(RB.steps, "removeNode", 13, (t == null) ? 666 : t.key, 666);
			} else {
				addStep(RB.steps, "removeNode", 14, 666, 666);
				t = u.right;
				addStep(RB.steps, "removeNode", 15, (t == null) ? 666 : t.key, 666);
			} /*
				 * se t non ha figli la link viene invocata con (u.parent,null,x)
				 */

			link(u.parent, t, x);
			addStep(RB.steps, "removeNode", 17, 666, (RB) null);
			if (t == null) {
				addStep(RB.steps, "removeNode", 17, u.key, this.getRoot(u));
				addStep(RB.steps, "removeNode", 18, u.key, 666);
				if (u.color == Color.BLACK) {
					addStep(RB.steps, "removeNode", 19, u.key, 666);
					balanceDelete(this.getRoot(u), t);
				}
			} else {
				addStep(RB.steps, "removeNode", 17, u.key, this.getRoot(t));
				addStep(RB.steps, "removeNode", 18, u.key, 666);
				if (u.color == Color.BLACK) {
					addStep(RB.steps, "removeNode", 19, u.key, 666);
					balanceDelete(this.getRoot(t), t);
				}
			}

			addStep(RB.steps, "removeNode", 20, 666, 555);
			/* il nodo da eliminare è root */
			if (u.parent == null) {
				addStep(RB.steps, "removeNode", 21, (t == null) ? 666 : t.key, 666);
				if (t != null) { /* suo figlio diventa root */
					t.parent = null;
					addStep(RB.steps, "removeNode", 22, 666, (RB) null);
					addStep(RB.steps, "removeNode", 22, t.key, this.getRoot(t));
				}

				addStep(RB.steps, "removeNode", 23, 666, t.key);
				addStep(RB.steps, "removeNode", 24, 666, t.key);

				addStep(RB.steps, "removeNode", 25, 666, (RB) null);
				addStep(RB.steps, "removeNode", 25, 666, 69);
				return t;
			}
		}
		addStep(RB.steps, "removeNode", 26, 666, (RB) null);
		addStep(RB.steps, "removeNode", 26, 666, 69);
		return this;
	}

	private void balanceDelete(RB T, RB t) {
		while ((t != T) || (t.color == Color.BLACK)) {
			RB p = t.parent;
			if (t == p.left) {
				RB f = p.right;
				RB ns = f.left;
				RB nd = f.right;
				if (f.color == Color.RED) // case 1
				{
					p.color = Color.RED;
					f.color = Color.BLACK;
					rotateLeft(p);
				} else {
					Color ns_color = (ns != null) ? ns.color : Color.BLACK;
					Color nd_color = (nd != null) ? nd.color : Color.BLACK;
					if (ns_color == nd_color && nd_color == Color.BLACK) {
						f.color = Color.RED;
						t = p;
					} else if (ns_color == Color.RED && nd_color == Color.BLACK) {
						ns.color = Color.BLACK;
						f.color = Color.RED;
						rotateRight(f);
					} else if (nd_color == Color.RED) // case4
					{
						f.color = p.color;
						p.color = Color.BLACK;
						nd.color = Color.BLACK;
						rotateLeft(p);
						// update root
						while (T.parent != null)
							T = T.parent;
						t = T;
					}
				}
			} else {
				RB f = p.left;
				RB ns = f.left;
				RB nd = f.right;
				if (f.color == Color.RED) // case 1
				{
					p.color = Color.RED;
					f.color = Color.BLACK;
					rotateRight(p);
				} else {
					Color ns_color = (ns != null) ? ns.color : Color.BLACK;
					Color nd_color = (nd != null) ? nd.color : Color.BLACK;
					if (ns_color == nd_color && nd_color == Color.BLACK) {
						f.color = Color.RED;
						t = p;
					} else if (nd_color == Color.RED && ns_color == Color.BLACK) {
						nd.color = Color.BLACK;
						f.color = Color.RED;
						rotateLeft(f);
					} else if (ns_color == Color.RED) // case4
					{
						f.color = p.color;
						p.color = Color.BLACK;
						ns.color = Color.BLACK;
						rotateRight(p);
						// update root
						while (T.parent != null)
							T = T.parent;
						t = T;
					}
				}
			}
			// update root
			while (T.parent != null)
				T = T.parent;
		}
		if (t != null)
			t.color = Color.BLACK;
	}

	public RB lookupNode(Integer j) {
		RB t = this;
		addStep(RB.steps, "lookup", 0, t.key, 666);
		while (t != null && t.key() != j) {
			addStep(RB.steps, "lookup", 1, 666, 666);
			if (j < t.key()) {
				t = t.left;
				addStep(RB.steps, "lookup", 2, t.key, 666);
			} else {
				t = t.right;
				addStep(RB.steps, "lookup", 4, t.key, 666);
			}
		}
		addStep(RB.steps, "lookup", 5, 666, 666);
		return t;
	}

	public RB lookupNodeNoStep(Integer j) {
		RB t = this;
		while (t != null && t.key() != j) {
			if (j < t.key()) {
				t = t.left;
			} else {
				t = t.right;
			}
		}
		return t;
	}

	public RB min() {
		RB t = this;
		addStep(RB.steps, "min", 0, t.key, 666);
		while (t.left() != null) {
			t = t.left;
			addStep(RB.steps, "min", 1, t.key, 666);
			addStep(RB.steps, "min", 0, t.key, 666);
		}
		addStep(RB.steps, "min", 2, t.key, 666);
		return t;
	}

	public RB max() {
		RB t = this;
		addStep(RB.steps, "max", 0, t.key, 666);
		while (t.right() != null) {
			t = t.right;
			addStep(RB.steps, "max", 1, t.key, 666);
			addStep(RB.steps, "max", 0, t.key, 666);
		}
		addStep(RB.steps, "max", 2, t.key, 666);
		return t;
	}

	public RB successorNode() {
		RB t = this;
		addStep(RB.steps, "successorNode", 0, t.key, 666);
		if (t == null) {
			addStep(RB.steps, "successorNode", 1, t.key, 666);
			return t;
		}

		addStep(RB.steps, "successorNode", 3, 666, 666);
		if (t.right() != null) {
			addStep(RB.steps, "successorNode", 4, 666, 666);
			return t.right().min();
		}
		addStep(RB.steps, "successorNode", 6, (t.parent == null) ? 666 : t.parent.key, 666);
		RB par = t.parent();
		addStep(RB.steps, "successorNode", 7, 666, 666);
		while (par != null && t == par.right) {
			t = par;
			addStep(RB.steps, "successorNode", 8, 666, 666);
			par = par.parent;
			addStep(RB.steps, "successorNode", 9, (par == null) ? 666 : par.key, 666);

			addStep(RB.steps, "successorNode", 7, 666, 666);
		}
		addStep(RB.steps, "successorNode", 11, 666, 666);
		return par;
	}

	public RB predecessorNode() {
		RB t = this;
		addStep(RB.steps, "predecessorNode", 0, t.key, 666);
		if (t == null) {
			addStep(RB.steps, "predecessorNode", 1, t.key, 666);
			return t;
		}

		addStep(RB.steps, "predecessorNode", 3, 666, 666);
		if (t.left != null) {
			addStep(RB.steps, "predecessorNode", 4, 666, 666);
			return t.left().max();
		}
		addStep(RB.steps, "predecessorNode", 6, (t.parent == null) ? 666 : t.parent.key, 666);
		RB par = t.parent;
		addStep(RB.steps, "predecessorNode", 7, 666, 666);
		while (par != null && t == par.left) {
			t = par;
			addStep(RB.steps, "predecessorNode", 8, 666, 666);
			par = t.parent;
			addStep(RB.steps, "predecessorNode", 9, (par == null) ? 666 : par.key, 666);

			addStep(RB.steps, "predecessorNode", 7, 666, 666);
		}
		addStep(RB.steps, "predecessorNode", 11, 666, 666);
		return par;
	}

}
