package controller;

import java.util.ArrayList;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.ABR;

public class ABRViewController extends NavigationController {
	/* costanti per Circle */
	private final static double R=20;
	private final static double ROOTX=150;
	private final static double ROOTY=0;
	/* costanti per Line */
	private final static double OFFSX=20;
	private final static double OFFSY=40;
	private final static double OFFEX=15;
	private final static double OFFEY=2;
	
	/* costante che definisce l'altezza massima dell'albero */
	private final static int MAXH=4;

	
	/* modello */
	private ABR abr = null;
	
	@FXML
	private Pane ABRView;
	
	public void handleRandomClick()  {
		
	}
	public void handleStepClick() {
	
	}
	@FXML
	public void handleInsertClick() {
		
		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");	
		 
		result.ifPresent(key -> {
			/* controlla che il valore inserito sia un intero */ 
			if (isStringInt(key)) {
				
				/* controlla che l'intero sia compreso tra -99 e 99 */
				Integer keyInt = Integer.parseInt(key);
				ABR p;
				if (keyInt >= -99 && keyInt <= 99) {					
							
			    	/* se l'albero(modello) è null disegna root */
			    	/* rootx 150 rooty 30 */
			    	if (abr == null) {
			    		abr = new ABR(keyInt, 0);
			    		abr.setX(ROOTX); abr.setY(ROOTY);
			    		drawNode(ROOTX,ROOTY,R, key);	
					 
			    	}else {
			    		/* inserisce nella struttura dati */
			    		abr.insertNode(keyInt, 0);
			    		/* salva riferimento all'ultimo nodo inserito */
			    		p = abr.lookupNode(keyInt);
			    		/* verifica che il nodo inserito non superi l'altezza massima stabilita */
			    		if(abr.getNodeHeight(p) <= MAXH) 
			    		{
			    			ArrayList<Double> coord;
			    			/* se il nodo è figlio sinistro del padre */
				    		if(p.parent().left() == p) {
				    			coord = getNodeEdgeCoordinates(p, 'l');				    			
				    			/* il nodo viene disegnato nella view */
				    			drawNode(coord.get(0), coord.get(1),R, key);
				    			/* l'arco viene disegnato nella view */
				    			drawLine(coord.get(2),coord.get(3),coord.get(4),coord.get(5));						    			
	                        /* se il nodo è figlio destro del padre */
				    		} else {
				    			coord = getNodeEdgeCoordinates(p, 'r');				    			
				    			drawNode(coord.get(0), coord.get(1),R, key);
				    			drawLine(coord.get(2),coord.get(3),coord.get(4),coord.get(5));	
				    		}
			    			
			    		} else {
			    			abr = abr.removeNode(keyInt);
			    			showAlert("Hai superato l'altezza massima consentita ("+MAXH+")");

			    		}			    		
			    	}				       
				    
				} else {
					showAlert("Scegli un intero tra -99 e 99");						
				}										
			} else 	{
				showAlert("L'input inserito non è un intero!");
			}
		    
		});
		
		
	}
	
	/* Dato un nodo e la sua posizione (dx/sx) rispetto al padre 
	 * restituisce le coordinate per rappresentare il nodo ed
	 * il rispettivo arco. Le coordinate sono nel formato
	 * [cx,cy,sx,sy,ex,ey] dove cx,cy sono le coordinate per 
	 * aggiungere lo stackpane che contiene un Circle (nodo) 
	 * ed un Text (key) e sx,sy,ex,ey sono le coordinate per 
	 * aggiungere l'arco
	 */
	private ArrayList<Double> getNodeEdgeCoordinates(ABR p, char side) {
		
		ArrayList<Double> coord = new ArrayList<Double>();
		double cx, cy, sx, sy, ex, ey;
		/* il nodo è figlio sinistro del padre */
		if (side == 'l') {
			cx = p.parent().getX()-40;
			cy = p.parent().getY()+50;
			p.setX(cx);
			p.setY(cy);
		    sx = p.parent().getX()+OFFSX;
			sy = p.parent().getY()+OFFSY;
			ex = cx + OFFEX;
			ey = cy + OFFEY;
		
		}else {
			/* il nodo è figlio destro del padre */
			cx = p.parent().getX()+40; 
			cy = p.parent().getY()+50;
			p.setX(cx);
			p.setY(cy);			
			sx = p.parent().getX()+OFFSX;
			sy = p.parent().getY()+OFFSY;
			ex = cx + OFFEX;
			ey = cy + OFFEY;
		}		
		coord.add(cx); coord.add(cy); 
		coord.add(sx); coord.add(sy);
		coord.add(ex); coord.add(ey);
		
		return coord;
		
	}
	public void handleRemoveClick() {
		
		Optional<String> result;
		result = showDialog("Inserisci la chiave:", "Valore chiave:");	
		
		/* verifica che l'input sia un intero */
		result.ifPresent(key -> {
			/* controlla che il valore inserito sia un intero */ 
			if (isStringInt(key)) {
				/* verifica che sia compreso tra -99 e 99 */
				Integer keyInt = Integer.parseInt(key);
				ABR p;
				if (keyInt >= -99 && keyInt <= 99) {
					/* verifica che il nodo con quella chiave esista nell'albero */
					ABR t = null;
					t = abr.lookupNode(keyInt);
					/* cerca lo stack pane che contiene il nodo con quella chiave */
					for (Node n : ABRView.getChildren()) {
						if (n instanceof StackPane) {
							ObservableList<Node> i = ((StackPane)n).getChildren();
							if (i.get(1) instanceof Text) {
								Text txt = (Text)i.get(1);
								if(txt.getText().equals(key)) {
									Circle c = (Circle)i.get(0);
									c.setFill(Color.RED);
									/* animazione dissolvenza del nodo */
									FadeTransition ft = nodeRemoveTransition(c,n, keyInt);
								    ft.play();									
								}								
							}
						
							System.out.println("true");
						}
					}
				
				} else {
					showAlert("Scegli un intero tra -99 e 99");						
				}
				
			} else {
				showAlert("L'input inserito non è un intero!");
				
			}
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
	
	public void handleRunClick() {
		
	}
	
	private FadeTransition nodeRemoveTransition(Circle c, Node n, Integer key) {
		FadeTransition ft = new FadeTransition(Duration.millis(3000), c);
	    ft.setFromValue(1.0);
	    ft.setToValue(0.0);
	    ft.setCycleCount(1);
	     
	    ft.setOnFinished(new EventHandler<ActionEvent>() {
	    	@Override
	        public void handle(ActionEvent actionEvent) {
	    		/* il nodo viene rimosso dalla view */
	    		ABRView.getChildren().remove(n);
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
	
	/* ristabilisce ricorsivamente le nuove coordinate dei nodi */
	private void restoreCoordinates(ABR t) {
		
		if (t != null) {
			// nodo root 
			if (t.parent() == null) {
				t.setX(ROOTX);
				t.setY(ROOTY);
			} else {
				if(t.parent().left() == t) {
					t.setX(t.parent().getX()-40);
					t.setY(t.parent().getY()+40);				
				}else {
					t.setX(t.parent().getX()+40);
					t.setY(t.parent().getY()+40);					
				}				
			}
			restoreCoordinates(t.left());
			restoreCoordinates(t.right());				
		}
	}
		
	
	private void drawTree(ABR t) {
		
		if(t != null) {
			// nodo root 
			if(t.parent() == null) {
				drawNode(ROOTX, ROOTY, R, t.key().toString());
			}else {
				ArrayList<Double> coord;
				if(t.parent().left() == t) {
	    			coord = getNodeEdgeCoordinates(t, 'l');				    			
	    			drawNode(coord.get(0), coord.get(1),R, t.key().toString());
	    			drawLine(coord.get(2),coord.get(3),coord.get(4),coord.get(5));				
				}else {
					coord = getNodeEdgeCoordinates(t, 'r');				    			
	    			drawNode(coord.get(0), coord.get(1),R, t.key().toString());
	    			drawLine(coord.get(2),coord.get(3),coord.get(4),coord.get(5));				
				}				
			}
			drawTree(t.left());
			drawTree(t.right());
		}
	}
	
	private void drawNode(double x, double y, double radius, String key) {
		
		Text text = new Text (key);
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
		Line line = new Line(sx,sy,ex,ey);
		line.setStrokeWidth(2);
		ABRView.getChildren().add(line);		
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
	
	
	
	private boolean isStringInt(String s)
	{
	    try
	    {
	        Integer.parseInt(s);
	        return true;
	    } catch (NumberFormatException ex)
	    {
	        return false;
	    }
	}

}
