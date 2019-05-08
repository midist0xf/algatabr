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

	public RB(Integer k, Integer v) {
		key = k;
		value = v;
		parent = null;
		right = null;
		left = null;
		color = Color.BLACK;
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

	public void rotateLeft() {
		RB x = this;
		RB y = x.right;
		RB p = x.parent;
		x.right = y.left;
		if (y.left != null)
			y.left.parent = x;
		y.left = x;
		x.parent = y;
		y.parent = p;
		if (p != null) {
			if (p.left == x)
				p.left = y;
			else
				p.right = y;
		}

	}

	public void rotateRight() {
		RB x = this;
		RB y = x.left;
		RB p = x.parent;
		x.left = y.right;
		if (y.right != null)
			y.right.parent = x;
		y.right = x;
		x.parent = y;
		y.parent = p;
		if (p != null) {
			if (p.left == x)
				p.left = y;
			else
				p.right = y;
		}
	}

	private void balanceInsert() {
		RB t = this;
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
					p.rotateLeft();
					t = p;
				} else if (t == p.left && p == n.right) // case 4.b
				{
					p.rotateRight();
					t = p;
				} else {
					if (t == p.left && p == n.left) // case 5.a
						n.rotateRight();
					else if (t == p.right && p == n.right)
						n.rotateLeft();
					p.color = Color.BLACK;
					n.color = Color.RED;
					t = null;
				}
			}
		}

	}

	public RB insertNode(Integer x, Integer v) {
		addStep(RB.steps, "insertNode", 0, this.key, 666);
		addStep(RB.steps, "insertNode", 1, 666, 666);
		
		RB s = null;
		RB u = this;

		addStep(RB.steps, "insertNode", 2, 666, 666);
		while (u != null && u.key != x) {
			addStep(RB.steps, "insertNode", 3, 666, 666);
			s = u;
			u = (x.compareTo(u.key) < 0) ? u.left : u.right;
			addStep(RB.steps, "insertNode", 4, (u != null)? u.key : 666, 666);
			addStep(RB.steps, "insertNode", 2, 666, 666);
		}

		addStep(RB.steps, "insertNode", 6, 666, 666);
		if (u != null && u.key == x) {
			addStep(RB.steps, "insertNode", 7, u.key, 666);
			u.value = v;
		} else {
			addStep(RB.steps, "insertNode", 8, 666, 666);
			addStep(RB.steps, "insertNode", 9, 666, 666);
			addStep(RB.steps, "insertNode", 10, 666, 666);
			RB n = new RB(x, v);
			n.color = Color.RED;
			s.link(n, x);

			addStep(RB.steps, "insertNode", 10, 666, (RB) null);
			addStep(RB.steps, "insertNode", 10, 666, n.getRoot(n));
			
			n.balanceInsert();
		}
		// return the new root
		u = this;
		while (u.parent != null)
			u = u.parent;

		addStep(RB.steps, "insertNode", 11, 666, (RB) null);
		addStep(RB.steps, "insertNode", 11, 666, u.key);
		addStep(RB.steps, "insertNode", 11, x, 666);


		return u;
	}

	private void addStep(ArrayList<ArrayList<String>> steps, String methodName, Integer codeLineToHighlight,
			Integer nodeValueToHighlight, RB rb) {
		// creo uno step da aggiungere alla lista di tutti gli steps
		ArrayList<String> step = new ArrayList<String>();

		step.add(methodName);
		step.add(codeLineToHighlight.toString());

		if (nodeValueToHighlight >= -99 && nodeValueToHighlight <= 99)
			step.add(nodeValueToHighlight.toString());
		else
			step.add("");

		// serializza l' rb in stringa
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);

			so.writeObject(rb);
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
		RB Tr = this;
		RB u = Tr.lookupNodeNoStep(x);
		
		addStep(RB.steps, "removeNode", 0, (u != null)? u.key : 666, 666);
		addStep(RB.steps, "removeNode", 1, 666, 666);
		
		if (u != null) {
			addStep(RB.steps, "removeNode", 2, u.key, 666);

			if (u.left != null && u.right != null) {
				RB s = u.right;
				addStep(RB.steps, "removeNode", 3, (s != null)? s.key:666, 666);

				addStep(RB.steps, "removeNode", 4, 666, 666);
				while (s.left != null) {
					s = s.left;
					addStep(RB.steps, "removeNode", 5, (s != null)? s.key:666, 666);
					addStep(RB.steps, "removeNode", 4, 666, 666);
				}

				addStep(RB.steps, "removeNode", 6, s.key, 666);

				u.key = s.key;

	 			addStep(RB.steps, "removeNode", 7, 666, (RB) null);
				addStep(RB.steps, "removeNode", 7, 666, u.getRoot(u));

				u.value = s.value;
				x = s.key;
				u = s;

				addStep(RB.steps, "removeNode", 8, 666, (RB) null);
				addStep(RB.steps, "removeNode", 9, 666, u.getRoot(u));
			}

			addStep(RB.steps, "removeNode", 11, 666, 666);
			RB t;
			addStep(RB.steps, "removeNode", 12, 666, 666);
			if (u.left != null && u.right == null) {
				t = u.left;
				addStep(RB.steps, "removeNode", 13, (t != null)? t.key:666, 666);
			} else {
				addStep(RB.steps, "removeNode", 14, 666, 666);
				t = u.right;
				addStep(RB.steps, "removeNode", 15, (t != null)? t.key:666, 666);
			}

			addStep(RB.steps, "removeNode", 17, 666, 666);
			if (u.parent == null) {
				// Tr is the new root
				addStep(RB.steps, "removeNode", 18, 666, 666);
				addStep(RB.steps, "removeNode", 19, 666, 666);

				Tr = t;
				Tr.parent = null;
			} else {
				addStep(RB.steps, "removeNode", 20, 666, 666);
				addStep(RB.steps, "removeNode", 21, 666, 666);
				u.parent.link(t, x);
				addStep(RB.steps, "removeNode", 21, 666, (RB) null);
				addStep(RB.steps, "removeNode", 21, 666, Tr.getRoot(Tr));
			}

			addStep(RB.steps, "removeNode", 22, 666, 666);

			if (u.color == Color.BLACK) {
				addStep(RB.steps, "removeNode", 23, 666, 666);
				this.balanceDelete(t, u.parent);
				addStep(RB.steps, "removeNode", 23, 666, (RB) null);
				addStep(RB.steps, "removeNode", 23, 666, Tr.getRoot(Tr));
			}
		}
		// return the new root
		while (Tr.parent != null)
			Tr = Tr.parent;

		addStep(RB.steps, "removeNode", 24, 666, (RB) null);
		addStep(RB.steps, "removeNode", 24, 666, 69);

		return Tr;
	}

	public void checkTree() {
		assert (this.color == Color.BLACK);
		this.checkChildren();
		blackHeight();
	}

	public void checkChildren() {
		if (this.color == Color.RED) {
			if (this.left != null)
				assert (this.left.color == Color.BLACK);
			if (this.right != null)
				assert (this.right.color == Color.BLACK);
		}
		if (this.left != null)
			this.left.checkChildren();
		if (this.right != null)
			this.right.checkChildren();
	}

	public int blackHeight() {
		int s = (this.left != null) ? this.left.blackHeight() : 1;
		int r = (this.right != null) ? this.right.blackHeight() : 1;
		assert (s == r);
		if (this.color == Color.BLACK)
			s++;
		return s;
	}

	private void balanceDelete(RB t, RB p) {
		RB Tr = this;
		while ((t == null) || (t != Tr && t.color == Color.BLACK)) {
			if (t != null)
				p = t.parent;
			if (t == p.left) {
				RB f = p.right;
				RB ns = f.left;
				RB nd = f.right;
				if (f.color == Color.RED) // case 1
				{
					p.color = Color.RED;
					f.color = Color.BLACK;
					p.rotateLeft();
				} else {
					Color ns_color = (ns != null) ? ns.color : Color.BLACK;
					Color nd_color = (nd != null) ? nd.color : Color.BLACK;
					if (ns_color == nd_color && nd_color == Color.BLACK) // case 2
					{
						f.color = Color.RED;
						t = p;
					} else if (ns_color == Color.RED && nd_color == Color.BLACK) // case 3
					{
						ns.color = Color.BLACK;
						f.color = Color.RED;
						f.rotateRight();
					} else if (nd_color == Color.RED) // case4
					{
						f.color = p.color;
						p.color = Color.BLACK;
						nd.color = Color.BLACK;
						p.rotateLeft();
						// update root
						while (Tr.parent != null)
							Tr = Tr.parent;
						t = Tr;
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
					p.rotateRight();
				} else {
					Color ns_color = (ns != null) ? ns.color : Color.BLACK;
					Color nd_color = (nd != null) ? nd.color : Color.BLACK;
					if (ns_color == nd_color && nd_color == Color.BLACK) // case 2
					{
						f.color = Color.RED;
						t = p;
					} else if (nd_color == Color.RED && ns_color == Color.BLACK) // case 3
					{
						nd.color = Color.BLACK;
						f.color = Color.RED;
						f.rotateLeft();
					} else if (ns_color == Color.RED) // case4
					{
						f.color = p.color;
						p.color = Color.BLACK;
						ns.color = Color.BLACK;
						p.rotateRight();
						// update root
						while (Tr.parent != null)
							Tr = Tr.parent;
						t = Tr;
					}
				}
			}
			// update root
			while (Tr.parent != null)
				Tr = Tr.parent;
		}
		if (t != null)
			t.color = Color.BLACK;
	}

	private void link(RB u, Integer x) {
		RB v = this;
		addStep(RB.steps, "link", 0, (u != null)? u.key : 666, 666);
		if (u != null) {
			u.parent = v;
			addStep(RB.steps, "link", 1, (u.parent != null)? u.parent.key : 666, 666);
		}
		addStep(RB.steps, "link", 2, (v != null)? v.key : 666, 666);
		if (v != null) {
			addStep(RB.steps, "link", 3, 666, 666);
			if (x.compareTo(v.key) < 0) {
				addStep(RB.steps, "link", 4, (u != null)? u.key : 666, 666);
				v.left = u;
			} else {
				addStep(RB.steps, "link", 5, (u != null)? u.key : 666, 666);
				addStep(RB.steps, "link", 6, (u != null)? u.key : 666, 666);
				v.right = u;
			}
		}
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
		addStep(RB.steps, "lookup", 5, t.key, 666);
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
