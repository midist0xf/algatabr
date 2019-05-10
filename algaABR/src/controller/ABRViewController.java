package controller;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.ABR;

import javafx.event.Event;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ABRViewController extends DataStuctureController {
	/* circle radius */
	private final static double R = 20;
	/* root node default coordinates */
	private final static double ROOTX = 150;
	private final static double ROOTY = 0;
	/* start/end line points offsets */
	private final static double OFFSX = 20;
	private final static double OFFSY = 40;
	private final static double OFFEX = 18;
	private final static double OFFEY = 0;

	/* maximum tree height */
	private final static int MAXH = 4;

	/* model */
	private ABR abr = null;

	/* store drawn circle, it's used by handleStepClick */
	ArrayList<Circle> drawnCircles = new ArrayList<Circle>();

	/* buttons */
	@FXML
	private Button randomButton;
	@FXML
	private Button insertButton;
	@FXML
	private Button successorButton;
	@FXML
	private Button predecessorButton;
	@FXML
	private Button minButton;
	@FXML
	private Button stepButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button lookupButton;
	@FXML
	private Button removeButton;
	@FXML
	private Button maxButton;
	@FXML
	private Button runButton;

	@FXML
	private Pane ABRView;

	@FXML
	public void handleInitialSteps() {
		if (!initialSteps.isEmpty()) {
			// lock buttons
			lockButtons(true);
			runButton.setDisable(true);

			// parse steps and execute them
			Pattern regex = Pattern.compile(";");

			for (String command : regex.split(initialSteps)) {
				switch (command.substring(0, command.indexOf("("))) {
				case "insertNode":
					Integer firstPar = Integer
							.valueOf(command.substring(command.indexOf("(") + 1, command.indexOf(",")));
					Integer secondPar = Integer
							.valueOf(command.substring(command.indexOf(",") + 1, command.indexOf(")")));

					if (abr == null) {
						abr = new ABR(firstPar, secondPar);
						abr.setX(ROOTX);
						abr.setY(ROOTY);
					} else {
						abr.insertNode(firstPar, secondPar);
						ABR p = abr.lookupNodeNoStep(firstPar);
						saveNodeRelativeCoordinates(p);
					}

					break;

				case "min":
					abr.min();
					break;

				case "max":
					abr.max();
					break;

				case "random":
					Integer[] arr = new Integer[99];
					for (int i = 0; i < arr.length; i++) {
						arr[i] = i;
					}
					Collections.shuffle(Arrays.asList(arr));
					abr = new ABR(arr[0], 0);
					abr.setX(ROOTX);
					abr.setY(ROOTY);
					for (int i = 1; i < Math.pow(2, MAXH); i++) {
						int value = choose(arr[i], -arr[i]);
						abr.insertNode(value, 0);
						ABR p = abr.lookupNode(value);
						saveNodeRelativeCoordinates(p);
						if (abr.getNodeHeight(p) > MAXH) {
							abr.removeNode(value);
						}
					}
					ABR.steps.clear();
					restoreCoordinates(abr);
					drawTree(abr);
					break;
				}
			}

			// clear initial steps 
			initialSteps = "";
		}
	}

	/**
	 * Handles user click on the insert button. Through a dialog asks to the user
	 * the key value to insert in the ABR. Validates the input and then modifies the
	 * ABR accordingly.
	 */
	@FXML
	public void handleInsertClick() {

		lockButtons(true);

		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");

		if (!result.isPresent())
			redraw();

		result.ifPresent(key -> {
			/* checks if the user input represents an integer */
			if (isStringInt(key)) {

				/* check if the integer is included between -99 and 99 */
				Integer keyInt = Integer.parseInt(key);
				if (isInRange(keyInt, -99, 99)) {

					/* if the tree is empty draws root node */
					if (abr == null) {
						abr = new ABR(keyInt, 0);
						abr.setX(ROOTX);
						abr.setY(ROOTY);
						drawTree(abr);
						lockButtons(false);
					} else {
						/* inserts the node in the model */
						abr.insertNode(keyInt, 0);
						/* get reference to the last inserted node */
						ABR p = abr.lookupNodeNoStep(keyInt);
						/*
						 * stores node coordinates relatively to its parent and relatively to its height
						 */
						saveNodeRelativeCoordinates(p);
						/* checks if the inserted node exceeds the chosen maximum height */
						if (abr.getNodeHeight(p) > MAXH) {
							abr = abr.removeNode(keyInt);
							showAlert("Hai superato l'altezza massima consentita (" + MAXH + ")");
							redraw();
						}
					}
				} else {
					showAlert("Scegli un intero tra -99 e 99");
					redraw();
				}
			} else {
				showAlert("L'input inserito non è un intero!");
				redraw();
			}
		});
	}

	/**
	 * Handles user click on the remove button. Through a dialog asks to the user
	 * the key value to remove from the ABR. Validates the input and then modifies
	 * the ABR accordingly.
	 */
	public void handleRemoveClick() {

		lockButtons(true);

		if (abr == null) {
			showAlert("L'albero è vuoto!");
			lockButtons(false);
		} else {
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");

			if (!result.isPresent())
				redraw();

			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						if (abr.lookupNodeNoStep(keyInt) != null) {
							/* the node is removed from the tree */
							abr = abr.removeNode(keyInt);
							/* updates coordinates */
							restoreCoordinates(abr);
						} else {
							showAlert("La chiave non è presente nell'albero!");
							redraw();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						redraw();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					redraw();
				}
			});
		}
	}

	/**
	 * Handles user click on the successor button. Through a dialog asks to the user
	 * the key value of which he wants to know the successor. Validates the input
	 * and then invokes the method on the ARB model accordingly.
	 */
	public void handleSuccessorClick() {

		if (abr == null) {
			showAlert("L'albero è vuoto!");
		} else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");

			if (!result.isPresent())
				redraw();

			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						ABR t = abr.lookupNodeNoStep(keyInt);
						if (t != null) {
							t = t.successorNode();
						} else {
							showAlert("La chiave non è presente nell'albero!");
							redraw();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						redraw();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					redraw();
				}
			});
		}
	}

	/**
	 * Handles user click on the predecessor button. Through a dialog asks to the
	 * user the key value of which he wants to know the predecessor. Validates the
	 * input and then invokes the method on the ARB model accordingly.
	 */
	public void handlePredecessorClick() {

		if (abr == null) {
			showAlert("L'albero è vuoto!");
		} else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						ABR t = abr.lookupNodeNoStep(keyInt);
						if (t != null) {
							t = t.predecessorNode();
						} else {
							showAlert("La chiave non è presente nell'albero!");
							redraw();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						redraw();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					redraw();
				}
			});
		}
	}

	/**
	 * Handles user click on the min button. Checks if the tree is null and invokes
	 * the min method on the ARB model.
	 */
	public void handleMinClick() {
		if (abr != null) {
			lockButtons(true);
			ABR t = abr.min();
		} else {
			showAlert("L'albero è vuoto!");
			redraw();
		}
	}

	/**
	 * Handles user click on the max button. Checks if the tree is null and invokes
	 * the max method on the ARB model.
	 */
	public void handleMaxClick() {
		if (abr != null) {
			lockButtons(true);
			ABR t = abr.max();
		} else {
			showAlert("L'albero è vuoto!");
			redraw();
		}
	}

	/**
	 * Handles user click on the lookup button. Through a dialog asks to the user
	 * the key value of the node to look for. Validates the input and then invokes
	 * the lookup method on the ARB model accordingly.
	 */
	public void handleLookupClick() {
		if (abr == null) {
			showAlert("L'albero è vuoto!");
		} else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						ABR t = abr.lookupNode(keyInt);
						if (t == null) {
							showAlert("La chiave non è presente nell'albero!");
							redraw();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						redraw();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					redraw();
				}
			});
		}
	}

	/**
	 * Handles user click on the random button. Generates 2^MAXH key values. Each
	 * values is included between -99 and 99. Inserts the keys in the ABR model and
	 * then draws the tree.
	 */
	public void handleRandomClick() {
		handleClearClick();
		Integer[] arr = new Integer[99];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		Collections.shuffle(Arrays.asList(arr));
		abr = new ABR(arr[0], 0);
		abr.setX(ROOTX);
		abr.setY(ROOTY);
		for (int i = 1; i < Math.pow(2, MAXH); i++) {
			int value = choose(arr[i], -arr[i]);
			abr.insertNode(value, 0);
			ABR p = abr.lookupNode(value);
			saveNodeRelativeCoordinates(p);
			if (abr.getNodeHeight(p) > MAXH) {
				abr.removeNode(value);
			}
		}
		drawTree(abr);

		ABR.steps.clear();
		lockButtons(false);
	}

	/**
	 * Handles user click on the step button. Removes the last element from the
	 * steps list. Each step is a 4-uple: [ method name, line number, key of the
	 * node to highlight, key of the node to modify graphically ] Highlights the
	 * line of pseudocode within the pseudocode view. Possibly draws the updated ARB
	 * model. Enables method buttons if the user executes the last step.
	 */
	@FXML
	public void handleStepClick() {
		ArrayList<String> step = ABR.steps.remove(0);

		// highlights pseudocode line
		lessonController.loadMethod(step.get(0), step.get(1));

		// highlights a node, if requested
		if (step.get(2) != "") {
			Circle c = searchNode(step.get(2));

			if (c != null && drawnCircles.contains(c) == false) {
				drawnCircles.add(c);
				InnerShadow is = new InnerShadow();
				DropShadow ds = new DropShadow();
				is.setColor(Color.YELLOW);
				ds.setColor(Color.YELLOW);
				is.setHeight(19.0f);

				c.setEffect(is);
				c.setEffect(ds);
			}
		}

		// draws nodes
		if (step.get(3) != "") {
			if (step.get(0) != "removeNode") {
				drawTree(abr);
			} else {
				// if there is a base64 serialized tree
				if (step.get(3).length() >= 4) {
					// deserialize the tree to print it
					try {
						byte b[] = Base64.getDecoder().decode(step.get(3));

						ByteArrayInputStream bi = new ByteArrayInputStream(b);
						ObjectInputStream si = new ObjectInputStream(bi);

						ABR abtree = (ABR) si.readObject();

						if (abtree != null) {
							drawTree(abtree);
						} else {
							ABRView.getChildren().clear();
							stepButton.fire();
						}

						si.close();
					} catch (Exception e) {
						drawTree(abr);
						System.out.println(e);
					}
				} else {
					drawTree(abr);
				}
			}
		}

		// if there are not more steps enables method buttons
		// and remove effects from circles.
		if (ABR.steps.isEmpty()) {
			lockButtons(false);

			for (Circle circle : drawnCircles) {
				circle.setEffect(null);
			}
			drawnCircles.clear();
		}
	}

	/**
	 * Handles user click on the run button. Check which method is currently saved
	 * in the steps list. Based on that retrieves the last step of the method
	 * execution, gets the key value and highlights the node accordingly.
	 */
	public void handleRunClick() {
		if (ABR.steps != null) {
			ArrayList<String> step;
			String method = ABR.steps.get(0).get(0);
			Circle c;
			FadeTransition ft;

			if (method.equals("removeNode")) {
				step = ABR.steps.get(1);
				if (step.get(2) != "") {
					c = searchNode(step.get(2).toString());
					ft = nodeRemoveTransition(c, Color.web("#EDA678"));
					ft.play();
				}
			} else if (method.equals("insertNode")) {
				step = ABR.steps.get(ABR.steps.size() - 1);
				if (step.get(2) != "") {
					drawTree(abr);
					c = searchNode(step.get(2).toString());
					ft = highlightNodeTransition(c, Color.web("#EDA678"));
					ft.play();
				}
			} else {
				step = ABR.steps.get(ABR.steps.size() - 1);
				c = searchNode(step.get(2).toString());
				ft = highlightNodeTransition(c, Color.web("#EDA678"));
				ft.play();
			}
		}
	}

	/**
	 * Handles user click on the clear button. Clears the view and set the model to
	 * null.
	 */
	public void handleClearClick() {
		abr = null;
		ABRView.getChildren().clear();
	}

	/**
	 * Creates a stack pane which contains a circle that represents a node and a
	 * text which contains the key value as a string. Adds the new created stack
	 * pane to the ABR view to make it visible.
	 * 
	 * @param x      coordinate to adjust the position of the node
	 * @param y      coordinate to adjust the position of the node
	 * @param radius the radius of the circle
	 * @param key    the key value of the node
	 */
	private void drawNode(double x, double y, double radius, String key) {

		Text text = new Text(key);
		text.setFill(Color.WHITE);
		Circle rootCircle = new Circle(20);

		StackPane stack = new StackPane();
		stack.setAlignment(Pos.CENTER);
		stack.getChildren().addAll(rootCircle, text);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		ABRView.getChildren().add(stack);
	}

	/**
	 * Creates a new line and adds it to the ABR view to make it visible.
	 * 
	 * @param sx x coordinate of the start point of the line
	 * @param sy y coordinate of the start point of the line
	 * @param ex x coordinate of the end point of the line
	 * @param ey y coordinate of the end point of the line
	 */
	private void drawLine(double sx, double sy, double ex, double ey) {
		Line line = new Line(sx, sy, ex, ey);
		line.setStrokeWidth(2);
		ABRView.getChildren().add(line);
	}

	/**
	 * Recursively iterates on the ABR model. Draws just a circle for the root node.
	 * Draws also a line (edge) for nodes which have a parent. Lines coordinates are
	 * computed accordingly to the parent position.
	 * 
	 * @param t ABR model to draw on the ABR view
	 */
	private void drawTree(ABR t) {

		if (t != null) {
			double sx, sy, ex, ey;

			drawNode(t.getX(), t.getY(), R, t.key().toString());
			if (t.parent() != null) {
				sx = t.parent().getX() + OFFSX;
				sy = t.parent().getY() + OFFSY;
				ex = t.getX() + OFFEX;
				ey = t.getY() + OFFEY;
				drawLine(sx, sy, ex, ey);
			}
			drawTree(t.left());
			drawTree(t.right());
		}
	}

	/**
	 * Clears the steps list and the ABR view. Draws the ABR tree accordingly to the
	 * current model. Finally enables method buttons.
	 */
	private void redraw() {
		ABR.steps.clear();
		ABRView.getChildren().clear();
		drawTree(abr);
		lockButtons(false);
	}

	/**
	 * Given a node computes its position coordinates relatively to the parent ones.
	 * Then saves them within the node.
	 * 
	 * @param p the node of which to store coordinates
	 */
	private void saveNodeRelativeCoordinates(ABR p) {
		if (p.parent() != null) {
			double cx, cy;
			if (p.parent().left() == p) {
				cx = p.parent().getX() - (ABRView.getWidth() / Math.pow(2, abr.getNodeHeight(p)));
				cy = p.parent().getY() + 50;
			} else {
				cx = p.parent().getX() + (ABRView.getWidth() / Math.pow(2, abr.getNodeHeight(p)));
				cy = p.parent().getY() + 50;
			}
			p.setX(cx);
			p.setY(cy);
		}
	}

	/**
	 * This method is used after ABR model structure is changed and it's invoked to
	 * updates coordinates value foreach node. Recursively iterates on the ABR
	 * model. Sets root default coordinates for root node. Sets relatively
	 * coordinates for the others nodes.
	 * 
	 * @param t ABR model on which to restore coordinates values
	 */
	private void restoreCoordinates(ABR t) {

		if (t != null) {
			// root node
			if (t.parent() == null) {
				t.setX(ROOTX);
				t.setY(ROOTY);
			} else {
				if (t.parent().left() == t) {
					t.setX(t.parent().getX() - (305.0 / Math.pow(2, abr.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				} else {
					t.setX(t.parent().getX() + (305.0 / Math.pow(2, abr.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				}
			}
			restoreCoordinates(t.left());
			restoreCoordinates(t.right());
		}
	}

	/**
	 * Given a key value as a string returns the Circle element of the ABR view
	 * which contains that value.
	 * 
	 * @param key the key value of the node to search
	 * @return the circle element which contains the key
	 */
	private Circle searchNode(String key) {
		Circle c = null;
		/* searches the stack pane that contains the node which stores the key */
		for (Node n : ABRView.getChildren()) {
			if (n instanceof StackPane) {
				ObservableList<Node> i = ((StackPane) n).getChildren();
				if (i.get(1) instanceof Text) {
					Text txt = (Text) i.get(1);
					if (txt.getText().equals(key)) {
						c = (Circle) i.get(0);
					}
				}
			}
		}
		return c;
	}

	/**
	 * Randomly returns one of two values.
	 * 
	 * @param a first value
	 * @param b second value
	 * @return one of the two values
	 */
	private int choose(int a, int b) {
		if (Math.random() < 0.5) {
			return a;
		} else {
			return b;
		}
	}

	/**
	 * Given a boolean parameter true(false), disables(enables) method buttons
	 * 
	 * @param b boolean value
	 */
	private void lockButtons(boolean b) {
		stepButton.setDisable(!b);
		runButton.setDisable(!b);

		randomButton.setDisable(b);
		insertButton.setDisable(b);
		successorButton.setDisable(b);
		predecessorButton.setDisable(b);
		minButton.setDisable(b);
		clearButton.setDisable(b);
		lookupButton.setDisable(b);
		removeButton.setDisable(b);
		maxButton.setDisable(b);

	}

	/**
	 * Given a string check if this string represents an Integer value and returns a
	 * boolean value accordingly.
	 * 
	 * @param s string to check
	 * @return true if the string represents an Integer, false otherwise
	 */
	private boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * Verifies if a value is included within a range.
	 * 
	 * @param value the value to check
	 * @param a     minimum bound
	 * @param b     maximum bound
	 * @return true if the value is within the range, false otherwise
	 */
	private boolean isInRange(int value, int a, int b) {
		return (value >= a && value <= b);
	}

	/**
	 * Sets lessonController field
	 * 
	 * @param lessonController the reference to a LessonController object
	 */
	public void setLessonController(LessonController lessonController) {
		this.lessonController = lessonController;
	}

	/**
	 * Creates a fade transition effect. If the transition is played it
	 * progressively fades a specific node of the tree.
	 * 
	 * @param c     the circle element on which to apply the transition
	 * @param color the color to show during the transition
	 * @return a fade transition object
	 */
	private FadeTransition nodeRemoveTransition(Circle c, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(500), c);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				ABR.steps.clear();
				ABRView.getChildren().clear();
				drawTree(abr);
				lockButtons(false);
			}
		});

		return ft;
	}

	/**
	 * Creates a fade transition effect. If the transition is played it
	 * progressively highlights a specific node of the tree.
	 * 
	 * @param c     the circle element on which to apply the transition
	 * @param color the color to show during the transition
	 * @return a fade transition object
	 */
	private FadeTransition highlightNodeTransition(Circle c, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(500), c);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.setCycleCount(1);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				ABR.steps.clear();
				ABRView.getChildren().clear();
				drawTree(abr);
				lockButtons(false);
			}
		});

		return ft;
	}

	/**
	 * Creates an alert and shows it to the user.
	 * 
	 * @param s the message to show within the alert
	 */
	private void showAlert(String s) {
		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle("Error alert");
		alert.setHeaderText(null);
		alert.setContentText(s);

		alert.showAndWait();
	}

	/**
	 * Create a dialog and shows it to the user.
	 * 
	 * @param header  the header text of the dialog
	 * @param content the content text of the dialog
	 * @return an Optional string value which stores the user's answer
	 */
	private Optional<String> showDialog(String header, String content) {

		TextInputDialog inputNodeDialog = new TextInputDialog();

		inputNodeDialog.setTitle("AlgaT");
		inputNodeDialog.setHeaderText(header);
		inputNodeDialog.setContentText(content);

		Optional<String> result = inputNodeDialog.showAndWait();
		return result;
	}
}
