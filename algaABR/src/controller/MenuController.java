package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MenuController extends NavigationController {
	
	@FXML
	private ComboBox<String> lessonsCBox;
	
	@FXML
	public void handleOkClick() {
		if (lessonsCBox.getValue() != null) {
			this.mainApp.gotoLesson(lessonsCBox.getValue());
		}
	}

}
