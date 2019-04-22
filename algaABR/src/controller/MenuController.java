package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MenuController extends Controller {
	
	@FXML
	private ComboBox<String> lessonsCBox;
	
	@FXML
	public void handleOkClick() {
		System.out.println(lessonsCBox.getValue());
		this.mainApp.changeScene("/view/" + lessonsCBox.getValue()  + ".fxml");
	}

}
