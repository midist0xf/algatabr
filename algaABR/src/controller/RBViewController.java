package controller;

import java.awt.List;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import org.omg.CORBA.PUBLIC_MEMBER;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.RB;

public class RBViewController extends DataStuctureController {
	/* costanti per Circle */
	private final static double R = 20;
	private final static double ROOTX = 150;
	private final static double ROOTY = 0;
	/* costanti per Line */
	private final static double OFFSX = 20;
	private final static double OFFSY = 40;
	private final static double OFFEX = 18;
	private final static double OFFEY = 0;

	/* costante che definisce l'altezza massima dell'albero */
	private final static int MAXH = 4;

	/* modello */
	private RB rb = null;

	/* serve per lo step button, memorizza i cerchi disegnati */
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
	public void handleInsertClick() {

		lockButtons(true);

		/* mostra dialog per inserimento chiave da inserire nell'albero */
		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");

		if (!result.isPresent())
			handleRunClick();

		result.ifPresent(key -> {
			/* controlla che il valore inserito sia un intero */
			if (isStringInt(key)) {

				/* controlla che l'intero sia compreso tra -99 e 99 */
				Integer keyInt = Integer.parseInt(key);
				if (keyInt >= -99 && keyInt <= 99) {

					/* se l'albero(modello) è null disegna root */
					if (rb == null) {
						rb = new RB(keyInt, 0);
						rb.setX(ROOTX);
						rb.setY(ROOTY);
						restoreCoordinates(rb);
						drawTree(rb);
						lockButtons(false);
					} else {
						/* inserisce nella struttura dati */
						rb.insertNode(keyInt, 0);
						/* salva riferimento all'ultimo nodo inserito */
						
						/* TODO: modificare insert o no? */
						rb = rb.getRoot(rb);
						
						RB p = rb.lookupNodeNoStep(keyInt);
						/*
						 * memorizza coordinate posizione nodo relativamente al padre , all'altezza
						 * nell'albero e alla larghezza della view
						 */
						saveNodeRelativeCoordinates(p);
						/* verifica che il nodo inserito non superi l'altezza massima stabilita */
						if (rb.getNodeHeight(p) <= MAXH) {
							// drawTree(abr);
						} else {
							rb = rb.removeNode(keyInt);
							showAlert("Hai superato l'altezza massima consentita (" + MAXH + ")");
							handleRunClick();
						}
					}
				} else {
					showAlert("Scegli un intero tra -99 e 99");
					handleRunClick();
				}
			} else {
				showAlert("L'input inserito non è un intero!");
				handleRunClick();
			}
		});
	}

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

	public void handleRemoveClick() {

		lockButtons(true);

		if (rb == null) {
			showAlert("L'albero è vuoto!");
			lockButtons(false);
		} else {
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");

			if (!result.isPresent())
				handleRunClick();

			result.ifPresent(key -> {
				/* verifica che il valore inserito sia un intero */
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					/* verifica che la chiave inserita sia compresa tra -99 e 99 */
					if (keyInt >= -99 && keyInt <= 99) {
						/* verifica che la chiave sia presente nell'albero */
						if (rb.lookupNodeNoStep(keyInt) != null) {

							/* il nodo viene eliminato dall'albero */
							rb = rb.removeNode(keyInt);
							/* aggiorna le coordinate */
							restoreCoordinates(rb);

							/*
							 * Circle c = searchNode(key); // animazione dissolvenza del nodo FadeTransition
							 * ft = nodeRemoveTransition(c, keyInt, Color.RED); ft.play();
							 */
						} else {
							showAlert("La chiave non è presente nell'albero!");
							handleRunClick();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						handleRunClick();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					handleRunClick();
				}
			});
		}
	}

	public void handleSuccessorClick() {

		if (rb == null) {
			showAlert("L'albero è vuoto!");
		} else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");

			if (!result.isPresent())
				handleRunClick();

			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						RB t = rb.lookupNodeNoStep(keyInt);
						if (t != null) {
							t = t.successorNode();
							if (t != null) {
								/*
								 * Circle c = searchNode(t.key().toString()); FadeTransition ft =
								 * highlightNodeTransition(c, Color.GREEN); ft.play();
								 */
							}
						} else {
							showAlert("La chiave non è presente nell'albero!");
							handleRunClick();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						handleRunClick();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					handleRunClick();
				}
			});
		}
	}

	public void handlePredecessorClick() {

		if (rb == null) {
			showAlert("L'albero è vuoto!");
		} else {
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
							if (t != null) {
								/*
								 * Circle c = searchNode(t.key().toString()); FadeTransition ft =
								 * highlightNodeTransition(c, Color.GREEN); ft.play();
								 */
							}
						} else {
							showAlert("La chiave non è presente nell'albero!");
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
					}
				} else {
					showAlert("L'input inserito non è un intero!");
				}
			});
		}
	}

	public void handleMinClick() {
		if (rb != null) {
			lockButtons(true);
			RB t = rb.min();
			/*
			 * Circle c = searchNode(t.key().toString()); FadeTransition ft =
			 * highlightNodeTransition(c,Color.GREEN); ft.play();
			 */
		} else {
			showAlert("L'albero è vuoto!");
			handleRunClick();
		}
	}

	public void handleMaxClick() {
		if (rb != null) {
			lockButtons(true);
			RB t = rb.max();
			/*
			 * Circle c = searchNode(t.key().toString()); FadeTransition ft =
			 * highlightNodeTransition(c,Color.GREEN); ft.play();
			 */
		} else {
			showAlert("L'albero è vuoto!");
			handleRunClick();
		}
	}

	public void handleLookupClick() {
		if (rb == null) {
			showAlert("L'albero è vuoto!");
		} else {
			lockButtons(true);
			Optional<String> result;
			result = showDialog("Inserisci la chiave:", "Valore chiave:");
			result.ifPresent(key -> {
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					if (isInRange(keyInt, -99, 99)) {
						RB t = rb.lookupNode(keyInt);
						if (t != null) {
							/*
							 * Circle c = searchNode(t.key().toString()); FadeTransition ft =
							 * highlightNodeTransition(c, Color.GREEN); ft.play();
							 */
						} else {
							showAlert("La chiave non è presente nell'albero!");
							handleRunClick();
						}
					} else {
						showAlert("Scegli un intero tra -99 e 99");
						handleRunClick();
					}
				} else {
					showAlert("L'input inserito non è un intero!");
					handleRunClick();
				}
			});
		}
	}

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
			rb.insertNode(value, 0);
			
			/* TODO: modificare insert o no? */
			rb = rb.getRoot(rb);
			
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

	private int choose(int a, int b) {
		if (Math.random() < 0.5) {
			return a;
		} else {
			return b;
		}
	}

	// < <nome metodo> <numero riga> <valore nodo da evidenziare> <valore nodo in
	// cui cambiare grafica> >
	@FXML
	public void handleStepClick() {
		ArrayList<String> step = RB.steps.remove(0);

		// mostra il metodo dello pseudocodice
		lessonController.loadMethod(step.get(0), step.get(1));

		// evidenzia un nodo se richiesto
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

		// effettua disegni sui nodi
		if (step.get(3) != "") {
			if (step.get(0) != "removeNode" && step.get(0) != "insertNode") {
				restoreCoordinates(rb);
				drawTree(rb);
			} else {
				// se c'e' un albero serializzato in base64
				if (step.get(3).length() >= 4) {
					// deserializza l'albero da stampare
					try {
						byte b[] = Base64.getDecoder().decode(step.get(3));

						ByteArrayInputStream bi = new ByteArrayInputStream(b);
						ObjectInputStream si = new ObjectInputStream(bi);

						RB rbtree = (RB) si.readObject();
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

		// attiva i buttons se ho finito steps
		// inoltre de-colora tutti i circle usati
		if (RB.steps.isEmpty()) {
			lockButtons(false);

			for (Circle circle : drawnCircles) {
				circle.setEffect(null);
			}
			drawnCircles.clear();
		}
	}

	public void handleRunClick() {
		RB.steps.clear();
		ABRView.getChildren().clear();
		restoreCoordinates(rb);
		drawTree(rb);
		lockButtons(false);
	}

	public void handleClearClick() {
		rb = null;
		ABRView.getChildren().clear();
	}

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

	private Circle searchNode(String key) {
		Circle c = null;
		/* cerca lo stack pane che contiene il nodo con quella chiave */
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

	/* ristabilisce ricorsivamente le nuove coordinate dei nodi */
	private void restoreCoordinates(RB t/* , int height */) {

		if (t != null) {
			// nodo root
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

	private void drawLine(double sx, double sy, double ex, double ey) {
		Line line = new Line(sx, sy, ex, ey);
		line.setStrokeWidth(2);
		ABRView.getChildren().add(line);
	}

	private FadeTransition nodeRemoveTransition(Circle c, Integer key, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);
		c.setFill(color);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				/* il nodo viene rimosso dalla view */
				ABRView.getChildren().remove(c.getParent());
				/* il nodo viene eliminato dall'albero */
				rb = rb.removeNode(key);
				/* aggiorna le coordinate */
				restoreCoordinates(rb);
				ABRView.getChildren().clear();
				/* si visualizza l'albero con la nuova struttura */
				restoreCoordinates(rb);
				drawTree(rb);
			}
		});

		return ft;
	}

	private FadeTransition highlightNodeTransition(Circle c, Paint color) {
		c.setFill(color);
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
		ft.setFromValue(0.1);
		ft.setToValue(1.0);
		ft.setCycleCount(1);

		return ft;
	}

	/* I metodi che seguono svolgono funzioni ausiliarie */
	/* TODO: probabilmente da rendere static e inserire in una classe Utility */
	private void showAlert(String s) {
		Alert alert = new Alert(AlertType.ERROR);

		alert.setTitle("Error alert");
		alert.setHeaderText(null);
		alert.setContentText(s);

		alert.showAndWait();
	}

	private Optional<String> showDialog(String header, String content) {

		TextInputDialog inputNodeDialog = new TextInputDialog();

		inputNodeDialog.setTitle("AlgaT - ABR");
		inputNodeDialog.setHeaderText(header);
		inputNodeDialog.setContentText(content);

		Optional<String> result = inputNodeDialog.showAndWait();
		return result;
	}

	private boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private boolean isInRange(int value, int a, int b) {
		return (value >= a && value <= b);
	}

	public void setLessonController(LessonController lessonController) {
		this.lessonController = lessonController;
	}
}
