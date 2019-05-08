package controller;

import java.io.File;
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
	
	
	/**
	 * Loads the names of the lessons within the combobox.
	 * Stores a reference to the json file which contains info about
	 * the lesssons.
	 */
	public void loadCBoxLessons() {
		try {
			File jsonConfig = new File("../lessons.json");
			Object parser; 
			
			if (jsonConfig.exists() == false) {
				parser = new JSONParser().parse(new FileReader("./lessons.json"));
			} else {  
				parser = new JSONParser().parse(new FileReader("../lessons.json"));
			}
				
			this.jsonRoot = (JSONObject) parser;
			
			Set lessons = jsonRoot.keySet();
			
			for (Iterator iter = lessons.iterator(); iter.hasNext();) {
				lessonsCBox.getItems().add((String) iter.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the click on the ok button. 
	 * Retrieves the value of the lesson chosen by the user and loads the 
	 * appropriate content.
	 */
	@FXML
	public void handleOkClick() {
		if (lessonsCBox.getValue() != null) {
			this.mainApp.gotoLesson(lessonsCBox.getValue(), jsonRoot);
		}
	}

}
