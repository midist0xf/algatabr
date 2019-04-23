package controller;

import java.util.Optional;

import javafx.collections.ObservableList;
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
import model.ABR;

public class ABRViewController extends NavigationController {
	/* costanti per Circle */
	private final static double R=20;
	private final static double ROOTX=150;
	private final static double ROOTY=30;
	/* costanti per Line */
	private final static double OFFSX=20;
	private final static double OFFSY=30;
	private final static double OFFEX=15;
	private final static double OFFEY=10;
	
	/* costante che definisce l'altezza massima dell'albero */
	private final static int MAXH=4;

	
	/* modello */
	private ABR<Integer> abr = null;
	
	@FXML
	private Pane ABRView;
	
	public void handleRandomClick()  {
		
	}
	public void handleStepClick() {
	
	}
	@FXML
	public void handleInsertClick() {
		
		TextInputDialog inputNodeDialog = new TextInputDialog();
		
		inputNodeDialog.setTitle("AlgaT - ABR");
		inputNodeDialog.setHeaderText("Inserisci la chiave:");
		inputNodeDialog.setContentText("Valore chiave:");
		 
		Optional<String> result = inputNodeDialog.showAndWait();
		 
		result.ifPresent(key -> {
			/* controlla che il valore inserito sia un intero */ 
			if (isStringInt(key)) {
				
				/* controlla che l'intero sia compreso tra -99 e 99 */
				Integer keyInt = Integer.parseInt(key);
				ABR<Integer> p;
				if (keyInt >= -99 && keyInt <= 99) {					
							
			    	/* se l'albero(modello) è null disegna root */
			    	/* rootx 150 rooty 30 */
			    	if (abr == null) {
			    		abr = new ABR<Integer>(keyInt, 0);
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
			    			
			    			/* se il nodo è figlio sinistro del padre */
				    		if(p.parent().left() == p) {
				    			/* calcolo delle nuove coordinate relativamente alla posizione del padre */
				    			double lx = p.parent().getX()-40;
				    			double ly = p.parent().getY()+50;
				    			/* le coordinate vengono salvate nel nodo */
				    			p.setX(lx);
				    			p.setY(ly);
				    			/* il nodo viene disegnato nella view */
				    			drawNode(lx, ly,R, key);
				    			/* calcolo coordinate dell'arco relativamente all'origine dello stack pane */
				    			double sx = p.parent().getX()+OFFSX;
				    			double sy = p.parent().getY()+OFFSY;
				    			double ex = lx + OFFEX;
				    			double ey = ly + OFFEY;
				    			/* l'arco viene disegnato nella view */
				    			drawLine(sx,sy,ex,ey);		
				    			
	                        /* se il nodo è figlio destro del padre */
				    		} else {
				    			double rx = p.parent().getX()+40; 
				    			double ry = p.parent().getY()+50;
				    			p.setX(rx);
				    			p.setY(ry);
				    			drawNode(rx, ry,R, key);			
				    			
				    			double sx = p.parent().getX()+OFFSX;
				    			double sy = p.parent().getY()+OFFSY;
				    			double ex = rx + OFFEX;
				    			double ey = ry + OFFEY;
				    			
				    			drawLine(sx,sy,ex,ey);
				    			
				    		}
			    			
			    		} else {
			    			/* TODO: rimuover il nodo aggiunto che supera l'altezza */
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
	public void handleRemoveClick() {
		
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
