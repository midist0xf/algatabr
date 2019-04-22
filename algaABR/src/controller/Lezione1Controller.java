package controller;

import javafx.fxml.FXML;

public class Lezione1Controller extends Controller {
	
	@FXML
	public void handleNextLessonClick() {
		
	}

	@FXML
	public void handleMenuClick() {
		this.mainApp.changeScene("/view/menuview.fxml");
	}

}
