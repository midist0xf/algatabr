package controller;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

public class ABRViewController {
	
	public void handleRandomClick()  {
		
	}
	public void handleStepClick() {
	
	}
	public void handleInsertClick() {
		
		TextInputDialog inputNodeDialog = new TextInputDialog();
		
		inputNodeDialog.setTitle("AlgaT - ABR");
		inputNodeDialog.setHeaderText("Inserisci la chiave:");
		inputNodeDialog.setContentText("Valore chiave:");
		 
		Optional<String> result = inputNodeDialog.showAndWait();
		 
		result.ifPresent(key -> {
			/* check if is an integer */
			if (isStringInt(key)) {
				System.out.print(key);				
			} else {
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
