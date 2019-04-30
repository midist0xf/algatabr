package controller;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MenuController extends NavigationController {
	
	@FXML private ComboBox<String> lessonsCBox;
	private JSONObject jsonRoot;
	
	// carica il file json in memoria, pronto per essere passato
	// ai vari controller della lezione. Carica inoltre i nomi
	// delle lezioni nel combobox
	public void loadCBoxLessons() {
		try {
			Object parser = new JSONParser().parse(new FileReader("../lessons.json"));
			this.jsonRoot = (JSONObject) parser;
			
			Set lessons = jsonRoot.keySet();
			
			for (Iterator iter = lessons.iterator(); iter.hasNext();) {
				lessonsCBox.getItems().add((String) iter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleOkClick() {
		if (lessonsCBox.getValue() != null) {
			this.mainApp.gotoLesson(lessonsCBox.getValue(), jsonRoot);
		}
	}

}
