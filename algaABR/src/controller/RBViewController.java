package controller;


import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;


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
import model.RB;

public class RBViewController extends DataStuctureController {
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
	private RB rb = null;

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

	/**
	   * Handles user click on the insert button.
	   * Through a dialog asks to the user the key value to insert in the RB.
	   * Validates the input and then modifies the RB accordingly.
	   */
	@FXML
	public void handleInsertClick() {

		lockButtons(true);
		
		insertButton.setStyle("-fx-background-color: #EDA678;\n-fx-text-fill: black;");
		
		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");

		if (!result.isPresent())
			redraw();

		result.ifPresent(key -> {
			/* checks if the user input represents an integer */
			if (isStringInt(key)) {

				/* check if the integer is included between -99 and 99 */
				Integer keyInt = Integer.parseInt(key);
				if(isInRange(keyInt, -99, 99)){

					/* if the tree is empty draws root node */
					if (rb == null) {
						rb = new RB(keyInt, 0);
						rb.setX(ROOTX);
						rb.setY(ROOTY);
						restoreCoordinates(rb);
						drawTree(rb);
						lockButtons(false);
					} else {
						/* inserts the node in the model */
						rb = rb.insertNode(keyInt, 0);						
						/* get reference to the last inserted node */
						RB p = rb.lookupNodeNoStep(keyInt);
						/* stores node coordinates relatively to its parent and relatively to its height */
						saveNodeRelativeCoordinates(p);
						/* checks if the inserted node exceeds the chosen maximum height */
						if (rb.getNodeHeight(p) > MAXH) {
							rb = rb.removeNode(keyInt);
							showAlert("Hai superato l'altezza massima consentita (" + MAXH + ")");
							redraw();
						}
					}
				} else {
					showAlert("Scegli un intero tra -99 e 99");redraw();}
			} else {showAlert("L'input inserito non è un intero!");redraw();}
		});
	}

	

	/**
	   * Handles user click on the remove button.
	   * Through a dialog asks to the user the key value to remove from the rb.
	   * Validates the input and then modifies the RB accordingly.
	   */	
	public void handleRemoveClick() {
		
		lockButtons(true);

		if (rb == null) {
			showAlert("L'albero è vuoto!");			
			lockButtons(false);
		}else {
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
	
			if (!result.isPresent())
				redraw();
		
			result.ifPresent(key -> {	
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if(isInRange(keyInt, -99, 99)){
						if (rb.lookupNodeNoStep(keyInt) != null) {
							/* the node is removed from the tree */
							rb = rb.removeNode(keyInt);
							/* updates coordinates */
							restoreCoordinates(rb);						
							} else { showAlert("La chiave non è presente nell'albero!");redraw();}
						} else {showAlert("Scegli un intero tra -99 e 99");redraw();}
					} else {showAlert("L'input inserito non è un intero!");redraw();}
			});			
		}			
	}



	/**
	   * Handles user click on the successor button.
	   * Through a dialog asks to the user the key value of which he wants to know the successor. 
	   * Validates the input and then invokes the method on the ARB model accordingly.
	   */
	public void handleSuccessorClick() {

		if (rb == null) {
			showAlert("L'albero è vuoto!");			
		}else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");

			if (!result.isPresent())
				redraw();

			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						RB t = rb.lookupNodeNoStep(keyInt);
						if (t != null) {
							t = t.successorNode();
						}else {showAlert("La chiave non è presente nell'albero!");redraw();}
					}else {showAlert("Scegli un intero tra -99 e 99");redraw();}
				}else {showAlert("L'input inserito non è un intero!");redraw();}					
			});			
		}	
	}

	/**
	   * Handles user click on the predecessor button.
	   * Through a dialog asks to the user the key value of which he wants to know the predecessor. 
	   * Validates the input and then invokes the method on the ARB model accordingly.
	   */
	public void handlePredecessorClick() {

		if (rb == null) {
			showAlert("L'albero è vuoto!");			
		}else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						RB t = rb.lookupNodeNoStep(keyInt);
						if (t != null) {
							t = t.predecessorNode();							
						}else {showAlert("La chiave non è presente nell'albero!"); redraw();}
					}else {showAlert("Scegli un intero tra -99 e 99"); redraw();}
				}else {showAlert("L'input inserito non è un intero!");redraw();}
			});
		}
	}

	/**
	   * Handles user click on the min button.
	   * Checks if the tree is null and invokes the min method on the ARB model.
	   */
	public void handleMinClick() {
		if (rb != null) {
			lockButtons(true);
			RB t = rb.min();	
		} else {
			showAlert("L'albero è vuoto!"); redraw();			
		}
	}

	/**
	   * Handles user click on the max button.
	   * Checks if the tree is null and invokes the max method on the ARB model.
	   */
	public void handleMaxClick() {
		if (rb != null) {
			lockButtons(true);
			RB t = rb.max();	
		} else {
			showAlert("L'albero è vuoto!");	redraw();
		}
	}

	/**
	   * Handles user click on the lookup button.
	   * Through a dialog asks to the user the key value of the node to look for.
	   * Validates the input and then invokes the lookup method on the ARB model accordingly.
	   */
	public void handleLookupClick() {
		if (rb == null) {
			showAlert("L'albero è vuoto!");			
		}else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
			result.ifPresent(key -> {		
					if (isStringInt(key)) {
						Integer keyInt = Integer.parseInt(key);
						if(isInRange(keyInt, -99, 99)){
							RB t = rb.lookupNode(keyInt);
							if (t == null) {
								showAlert("La chiave non è presente nell'albero!"); redraw();																	 
							}
						}else {showAlert("Scegli un intero tra -99 e 99"); redraw();}
					}else {showAlert("L'input inserito non è un intero!"); redraw();}
			});			
		}
	}	

	/**
	   * Handles user click on the random button.
	   * Generates 2^MAXH key values. Each values is included between -99 and 99.
	   * Inserts the keys in the RB model and then draws the tree.
	   */
	public void handleRandomClick() {
		handleClearClick();
		Integer[] arr = new Integer[99];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		Collections.shuffle(Arrays.asList(arr));
		rb = new RB(arr[0], 0);
		rb.setX(ROOTX);
		rb.setY(ROOTY);
		for (int i = 1; i < 16; i++) {
			int value = choose(arr[i], -arr[i]);
			rb = rb.insertNode(value, 0);			
			RB p = rb.lookupNodeNoStep(value);
			saveNodeRelativeCoordinates(p);
			if (rb.getNodeHeight(p) > MAXH) {
				rb.removeNode(value);
			}
		}
		restoreCoordinates(rb);
		drawTree(rb);

		RB.steps.clear();
		lockButtons(false);
	}
	
	/**
	   * Handles user click on the step button.
	   * Removes the last element from the steps list. Each step is a 4-uple:
	   * [ method name, line number, key of the node to highlight, key of the node to modify graphically ]
	   * Highlights the line of pseudocode within the pseudocode view.
	   * Possibly draws the updated ARB model.
	   * Enables method buttons if the user executes the last step. 
	   */
	@FXML
	public void handleStepClick() {
		ArrayList<String> step = RB.steps.remove(0);

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
			if (step.get(0) != "removeNode"  && step.get(0) != "insertNode") {
				restoreCoordinates(rb);
				drawTree(rb);
			} else {
				// if there is a base64 serialized tree
				if (step.get(3).length() >= 4) {
					// deserialize the tree to print it
					try {
						byte b[] = Base64.getDecoder().decode(step.get(3));
						
						ByteArrayInputStream bi = new ByteArrayInputStream(b);
						ObjectInputStream si = new ObjectInputStream(bi);
						
						RB rbtree =(RB) si.readObject();
						si.close();
						restoreCoordinates(rbtree);

						if (rbtree != null) {
							drawTree(rbtree);
						} else {
							ABRView.getChildren().clear();
							stepButton.fire();
						}
						
					} catch (Exception e) {
						restoreCoordinates(rb);
						drawTree(rb);
						System.out.println(e);
					}
				} else {
					restoreCoordinates(rb);
					drawTree(rb);
				}
			}
		}
		
		// if there are not more steps enables method buttons
		// and remove effects from circles.
		if (RB.steps.isEmpty()) {
			lockButtons(false);
			
			for (Circle circle : drawnCircles) {
				circle.setEffect(null);
			}
			drawnCircles.clear();
		}
	}
	
	/**
	   * Handles user click on the run button.
	   * Check which method is currently saved in the steps list.
	   * Based on that retrieves the last step of the method execution,
	   * gets the key value and highlights the node accordingly.
	   */
	public void handleRunClick() {
		if (RB.steps != null) {
			ArrayList<String> step;
            String method = RB.steps.get(0).get(0);
            Circle c; FadeTransition ft;
            
			if ( method.equals("removeNode")) {
				step = RB.steps.get(0);
				if(step.get(2) != "") {
					c = searchNode(step.get(2).toString());
					ft = nodeRemoveTransition(c, Color.web("#EDA678"));
					ft.play();
				}
			} else if(method.equals("insertNode")) {
				step = RB.steps.get(RB.steps.size()-1);	
				if(step.get(2) != "") {
					ABRView.getChildren().clear();
					restoreCoordinates(rb);
					drawTree(rb);
					c = searchNode(step.get(2).toString());
					ft = highlightNodeTransition(c, Color.web("#EDA678"));
					ft.play();
				}								
			} else {
				step = RB.steps.get(RB.steps.size()-1);
				c = searchNode(step.get(2).toString());
				ft = highlightNodeTransition(c, Color.web("#EDA678"));
				ft.play();				
			}			
		}
	}

	
	/**
	   * Handles user click on the clear button.
	   * Clears the view and set the model to null.
	   */
	public void handleClearClick() {
		rb = null;
		ABRView.getChildren().clear();
	}

	/**
	   * Creates a stack pane which contains a circle that represents
	   * a node and a text which contains the key value as a string.
	   * Adds the new created stack pane to the RB view to make it visible.
	   * @param x coordinate to adjust the position of the node
	   * @param y coordinate to adjust the position of the node
	   * @param radius the radius of the circle
	   * @param key the key value of the node
	   */	
	private void drawNode(double x, double y, double radius, String key, model.RB.Color color) {

		Text text = new Text(key);
		text.setFill(Color.WHITE);
		Circle rootCircle = new Circle(20);

		switch (color) {
		case BLACK:
			rootCircle.setFill(Color.BLACK);
			break;

		case RED:
			rootCircle.setFill(Color.RED);
			break;
		}

		StackPane stack = new StackPane();
		stack.setAlignment(Pos.CENTER);
		stack.getChildren().addAll(rootCircle, text);
		stack.setLayoutX(x);
		stack.setLayoutY(y);

		ABRView.getChildren().add(stack);
	}

	/**
	   * Creates a new line and adds it to the RB view to make it visible.
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
	   * Recursively iterates on the RB model.
	   * Draws just a circle for the root node.
	   * Draws also a line (edge) for nodes which have a parent.
	   * Lines coordinates are computed accordingly to the parent position. 
	   * @param t RB model to draw on the RB view 
	   */
	private void drawTree(RB t) {

		if (t != null) {
			double sx, sy, ex, ey;

			drawNode(t.getX(), t.getY(), R, t.key().toString(), t.getColor());
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
	   * Clears the steps list and the RB view.
	   * Draws the RB tree accordingly to the current model.
	   * Finally enables method buttons.
	*/
	private void redraw() {
		RB.steps.clear();
		ABRView.getChildren().clear();
		drawTree(rb);
		lockButtons(false);
	}
	
	/**
	 * Given a node computes its position coordinates relatively to the parent ones. 
	 * Then saves them within the node.
	 * @param p the node of which to store coordinates
	 */
	private void saveNodeRelativeCoordinates(RB p) {
		if (p.parent() != null) {
			double cx, cy;
			if (p.parent().left() == p) {
				cx = p.parent().getX() - (ABRView.getWidth() / Math.pow(2, rb.getNodeHeight(p)));
				cy = p.parent().getY() + 50;
			} else {
				cx = p.parent().getX() + (ABRView.getWidth() / Math.pow(2, rb.getNodeHeight(p)));
				cy = p.parent().getY() + 50;
			}
			p.setX(cx);
			p.setY(cy);
		}
	}
	
	/**
	 * This method is used after RB model structure is changed and it's invoked to
	 * updates coordinates value foreach node. 
	 * Recursively iterates on the RB model.
	 * Sets root default coordinates for root node.
	 * Sets relatively coordinates for the others nodes.
	 * @param t RB model on which to restore coordinates values
	 */
	private void restoreCoordinates(RB t) {

		if (t != null) {
			// root node
			if (t.parent() == null) {
				t.setX(ROOTX);
				t.setY(ROOTY);
			} else {
				if (t.parent().left() == t) {
					t.setX(t.parent().getX() - (ABRView.getWidth() / Math.pow(2, rb.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				} else {
					t.setX(t.parent().getX() + (ABRView.getWidth() / Math.pow(2, rb.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				}
			}
			restoreCoordinates(t.left());
			restoreCoordinates(t.right());
		}
	}	
	
	/**
	 * Given a key value as a string returns the Circle element of the RB view
	 * which contains that value.
	 * @param key the key value of the node to search
	 * @return the circle element which contains the key 
	 */
	private Circle searchNode(String key) {
		Circle c = null;
		/* searches the stack pane that contains the node which stores the key  */
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
	 * @param a first value
	 * @param b second value
	 * @return one of the two values
	 */
	private int choose(int a, int b) {
		if (Math.random() < 0.5) {return a;} else {return b;}
	}

	/**
	 * Given a boolean parameter true(false), disables(enables) method buttons
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
	 * Given a string check if this string represents an Integer value
	 * and returns a boolean value accordingly.
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
	 * @param value the value to check
	 * @param a minimum bound
	 * @param b maximum bound
	 * @return true if the value is within the range, false otherwise
	 */
	private boolean isInRange(int value, int a, int b) {
		return (value >= a && value <= b);
	}

	/**
	 * Sets lessonController field
	 * @param lessonController the reference to a LessonController object 
	 */
	public void setLessonController(LessonController lessonController) {
		this.lessonController = lessonController;
	}
	
	/**
	 * Creates a fade transition effect.
	 * If the transition is played it progressively fades a specific node of the tree.  
	 * @param c the circle element on which to apply the transition
	 * @param color the color to show during the transition
	 * @return a fade transition object
	 */
	private FadeTransition nodeRemoveTransition(Circle c, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				RB.steps.clear();
				ABRView.getChildren().clear();
				drawTree(rb);
				lockButtons(false);	
			}
		});

		return ft;
	}
	
	/**
	 * Creates a fade transition effect.
	 * If the transition is played it progressively highlights a specific node of the tree. 
	 * @param c the circle element on which to apply the transition
	 * @param color the color to show during the transition
	 * @return a fade transition object
	 */
	private FadeTransition highlightNodeTransition(Circle c, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.setCycleCount(1);
		
		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				RB.steps.clear();
				ABRView.getChildren().clear();
				drawTree(rb);
				lockButtons(false);
			}
		});
		
		return ft;
	}
	
	/**
	 * Creates an alert and shows it to the user.
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
	 * @param header the header text of the dialog
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
