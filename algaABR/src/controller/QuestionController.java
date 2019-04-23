package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

public class QuestionController {
	
	public int lessonNumber = 0;
	
	@FXML Text questionText;
	@FXML RadioButton firstAnswerRadioB;
	@FXML RadioButton secondAnswerRadioB;
	@FXML RadioButton thirdAnswerRadioB;
	
	@FXML
	public void handleAnswerConfirm() {
		System.out.println(lessonNumber);
	}
	
	private void loadQuestions() {
		
	}
	
}
