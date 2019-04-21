package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class MenuController extends Controller {
	
	@FXML
	private ComboBox<String> lessonsCBox;
	
	@FXML
	public void initialize() {
		lessonsCBox.setItems(FXCollections.observableArrayList(
				new String("Lezione 0"),
				new String("Lezione 1")
				));
	}
	
	@FXML
	public void handleOkClick() {
		this.mainApp.changeScene("/view/" + lessonsCBox.getValue()  + ".fxml");
	}

}
