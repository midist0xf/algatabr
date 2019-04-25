package controller;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PseudocodeController {
	
	@FXML private ComboBox<String> functionsCBox;
	@FXML private TextArea codeTextArea;
	
	private Hashtable<String, String> functionsHTable = new Hashtable<String, String>();
	
	@FXML
	public void handleShowCode() {
		codeTextArea.setText(functionsHTable.get(functionsCBox.getValue()));
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
	
}
