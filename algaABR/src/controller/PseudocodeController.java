package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PseudocodeController {
	
	public int lessonNumber = 0;
	
	@FXML
	Button testBtn;
	
	@FXML
	public void handleClick() {
		System.out.println("ciaobigmister");
		System.out.println(lessonNumber);
	}
	
}
