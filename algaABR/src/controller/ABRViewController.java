package controller;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.ABR;

public class ABRViewController {
	/* costanti per Circle */
	private final static double R = 20;
	private final static double ROOTX = 150;
	private final static double ROOTY = 0;
	/* costanti per Line */
	private final static double OFFSX = 20;
	private final static double OFFSY = 40;
	private final static double OFFEX = 15;
	private final static double OFFEY = 2;

	/* costante che definisce l'altezza massima dell'albero */
	private final static int MAXH = 4;

	/* modello */
	private ABR abr = null;

	/* buttons */
	@FXML private Button randomButton;
	@FXML private Button insertButton;
	@FXML private Button successorButton;
	@FXML private Button predecessorButton;
	@FXML private Button minButton;
	@FXML private Button stepButton;
	@FXML private Button clearButton;
	@FXML private Button lookupButton;
	@FXML private Button removeButton;
	@FXML private Button maxButton;
	@FXML private Button runButton;
	
	/* lista steps 
	 * < <nome metodo corrente> <numero riga da evidenziare> <valore nodo da evidenziare> <valore nodo in cui cambiare grafica> >
	 * */
	private java.util.List<java.util.List<String>> steps = new ArrayList<java.util.List<String>>();
	
	/* lesson controller per comunicare con gli altri controllers*/
	private LessonController lessonController;
	
	@FXML
	private Pane ABRView;

	@FXML
	public void handleInsertClick() {

		lockButtons(true);

		/* mostra dialog per inserimento chiave da inserire nell'albero */
		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");

		result.ifPresent(key -> {
			/* controlla che il valore inserito sia un intero */
			if (isStringInt(key)) {

				/* controlla che l'intero sia compreso tra -99 e 99 */
				Integer keyInt = Integer.parseInt(key);
				if (keyInt >= -99 && keyInt <= 99) {

					/* se l'albero(modello) è null disegna root */
					if (abr == null) {
						abr = new ABR(keyInt, 0);
						abr.setX(ROOTX);
						abr.setY(ROOTY);
						drawNode(ROOTX, ROOTY, R, key);
						lockButtons(false);
					} else {
						/* inserisce nella struttura dati */
						abr.insertNode(keyInt, 0, steps);
						/* salva riferimento all'ultimo nodo inserito */
						ABR p = abr.lookupNode(keyInt);
						/* memorizza coordinate posizione nodo relativamente al padre
						 * , all'altezza nell'albero e alla larghezza della view */
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

						/* verifica che il nodo inserito non superi l'altezza massima stabilita */
						if (abr.getNodeHeight(p) <= MAXH) {
							//drawTree(abr);
						} else {
							abr = abr.removeNode(keyInt);
							showAlert("Hai superato l'altezza massima consentita (" + MAXH + ")");
						}
					}
				} else {showAlert("Scegli un intero tra -99 e 99");}
			} else {showAlert("L'input inserito non è un intero!");}
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

	public void handleRemoveClick() {

		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");

		result.ifPresent(key -> {
			/* controlla che l'albero non sia vuoto */
			if (abr != null) {
				/* verifica che il valore inserito sia un intero */
				if (isStringInt(key)) {
					Integer keyInt = Integer.parseInt(key);
					/* verifica che la chiave inserita sia compresa tra -99 e 99 */
					if (keyInt >= -99 && keyInt <= 99) {
						/* verifica che la chiave sia presente nell'albero */
						if (abr.lookupNode(keyInt) != null) {
							Circle c = searchNode(key);
							if (c != null) {
								c.setFill(Color.RED);
								/* animazione dissolvenza del nodo */
								FadeTransition ft = nodeRemoveTransition(c, keyInt);
								ft.play();
							}
						} else { showAlert("La chiave non è presente nell'albero!");}
					} else {showAlert("Scegli un intero tra -99 e 99");}
				} else {showAlert("L'input inserito non è un intero!");}
			} else {showAlert("L'albero è vuoto!");}
		});
	}

	public void handleSuccessorClick() {

	}

	public void handlePredecessorClick() {

	}

	public void handleMinClick() {

	}

	public void handleMaxClick() {

	}

	public void handleLookupClick() {

	}

	public void handleRandomClick() {

	}

	// < <nome metodo> <numero riga> <valore nodo da evidenziare> <valore nodo in cui cambiare grafica> >
	public void handleStepClick() {
		java.util.List<String> step = steps.remove(0);

		lessonController.loadMethod(step.get(0), step.get(1));
	}

	public void handleRunClick() {
	}

	public void handleClearClick() {
		abr = null;
		ABRView.getChildren().clear();
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
	private void restoreCoordinates(ABR t/* , int height */) {

		if (t != null) {
			// nodo root
			if (t.parent() == null) {
				t.setX(ROOTX);
				t.setY(ROOTY);
			} else {
				if (t.parent().left() == t) {
					t.setX(t.parent().getX() - (ABRView.getWidth() / Math.pow(2, abr.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				} else {
					t.setX(t.parent().getX() + (ABRView.getWidth() / Math.pow(2, abr.getNodeHeight(t))));
					t.setY(t.parent().getY() + 50);
				}
			}
			restoreCoordinates(t.left());
			restoreCoordinates(t.right());
		}
	}

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

	private void drawLine(double sx, double sy, double ex, double ey) {
		Line line = new Line(sx, sy, ex, ey);
		line.setStrokeWidth(2);
		ABRView.getChildren().add(line);
	}

	private FadeTransition nodeRemoveTransition(Circle c, Integer key) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setCycleCount(1);

		ft.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				/* il nodo viene rimosso dalla view */
				ABRView.getChildren().remove(c.getParent());
				/* il nodo viene eliminato dall'albero */
				abr = abr.removeNode(key);
				/* aggiorna le coordinate */
				restoreCoordinates(abr);
				ABRView.getChildren().clear();
				/* si visualizza l'albero con la nuova struttura */
				drawTree(abr);
			}
		});

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

	public void setLessonController(LessonController lessonController) {
		this.lessonController = lessonController;
	}
}
