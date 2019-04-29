package controller;

import java.awt.TextField;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PseudocodeController {
	
	@FXML private ComboBox<String> functionsCBox;
	@FXML private TextFlow codeTextFlow;
	
	private Hashtable<String, String> functionsHTable = new Hashtable<String, String>();
	
	@FXML
	public void handleShowCode() {
		codeTextFlow.getChildren().clear();

		Text t = new Text(functionsHTable.get(functionsCBox.getValue()));
		codeTextFlow.getChildren().add(t);
		
		highlightLine(3);
	}

	public void loadCodes(JSONObject jFunctions) {
		try {
			Set interfaces = jFunctions.keySet();
			
			for (Iterator iter = interfaces.iterator(); iter.hasNext();) {
				String current = (String) iter.next();

				functionsHTable.put(current, (String) jFunctions.get(current));
				functionsCBox.getItems().add((String) current);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// NB le righe di codice sono numerate a partire da 0 
	public void highlightLine(int lineNumber) {
		Pattern regex = Pattern.compile("\\n");
		String[] stringSplit = regex.split(functionsHTable.get(functionsCBox.getValue()));
		Text[] textSplit = new Text[stringSplit.length];
		
		for (int i = 0; i < stringSplit.length; i++) {
			textSplit[i] = new Text(stringSplit[i] + "\n");
		}
		
		textSplit[lineNumber].setFill(Color.CHOCOLATE);
		textSplit[lineNumber].setFont(new Font("Times New Roman", 17));
		
		codeTextFlow.getChildren().setAll(textSplit);
	}
	
}
