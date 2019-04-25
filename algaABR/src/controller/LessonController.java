package controller;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class LessonController extends NavigationController {
	
	private JSONObject jsonRoot;
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;

	public void setLesson(String lessonName, JSONObject jsonRoot) {
		this.jsonRoot = jsonRoot;
		
		JSONObject selectedLesson = (JSONObject) jsonRoot.get(lessonName);
		
		showGreetingMessage(selectedLesson.get("DescrizioneLezione").toString());

		questionController.loadQuestions((JSONObject) selectedLesson.get("DomandeRiposteSpiegazioni"));
		questionController.setMainApp(this.mainApp);
		
		pseudocodeController.loadCodes((JSONObject) selectedLesson.get("Pseudocodice"));
	}

	private void showGreetingMessage(String message) {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		result.setTitle("Benvenuto");
		result.setHeaderText("Scopo della lezione");
		result.setContentText(message);
		result.show();
	}

	
}
