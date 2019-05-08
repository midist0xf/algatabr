package controller;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class PseudocodeController {
	
	@FXML private ComboBox<String> functionsCBox;
	@FXML private TextFlow codeTextFlow;
	
	private Hashtable<String, String> functionsHTable = new Hashtable<String, String>();
	
	/**
	 * Shows the pseudocode of a specific method
	 */
	@FXML
	public void handleShowCode() {
		codeTextFlow.getChildren().clear();

		Text t = new Text(functionsHTable.get(functionsCBox.getValue()));
		codeTextFlow.getChildren().add(t);
	}

	/**
	 * Stores <key,value> associations within an hashtable where 
	 * key is the name of a method and value is its pseudocode.
	 * Inserts names of methods in the combobox.
	 * @param jFunctions jsonObject which contains methods names and pseudocodes 
	 */
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
	
	/**
	 * Highlights a line of the pseudocode.
	 * Line numbers start from 0.
	 * @param lineNumber the line number to highlight
	 */
	public void highlightLine(int lineNumber) {
		Pattern regex = Pattern.compile("\\n");
		String[] stringSplit = regex.split(functionsHTable.get(functionsCBox.getValue()));
		Text[] textSplit = new Text[stringSplit.length];
		
		for (int i = 0; i < stringSplit.length; i++) {
			textSplit[i] = new Text(stringSplit[i] + "\n");
		}
		
		if (lineNumber < textSplit.length) {
			textSplit[lineNumber].setFill(Color.DARKORANGE);
			//textSplit[lineNumber].setFont(new Font("Times New Roman", 17));
		}

		codeTextFlow.getChildren().setAll(textSplit);
	}

	/**
	 * Loads the appropriate method within the combobox and invokes
	 * a procedure to higlight a specific line of the pseudocode associated
	 * with that method.
	 * @param methodName the name of the method
	 * @param lineNumber the number of the line of the pseudocode to highlight
	 */
	public void showMethod(String methodName, int lineNumber) {
		for (String val : functionsHTable.keySet()) {
			if (val.contains(methodName)) {
				functionsCBox.setValue(val);
				highlightLine(lineNumber);
				break;
			}
		}
	}
	
}
