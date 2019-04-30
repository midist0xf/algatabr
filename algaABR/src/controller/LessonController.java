package controller;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class LessonController extends NavigationController {
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;
	@FXML private ABRViewController controlController; // TODO: aggiungere controllo dinamico se abr o rb

	public void setLesson(String lessonName, JSONObject jsonRoot) {
		JSONObject selectedLesson = (JSONObject) jsonRoot.get(lessonName);
		
		showGreetingMessage(selectedLesson.get("DescrizioneLezione").toString());

		questionController.loadQuestions((JSONObject) selectedLesson.get("DomandeRiposteSpiegazioni"));
		questionController.setMainApp(this.mainApp);
		
		pseudocodeController.loadCodes((JSONObject) selectedLesson.get("Pseudocodice"));
		
		controlController.setLessonController(this);
	}

	private void showGreetingMessage(String message) {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		result.setTitle("Benvenuto");
		result.setHeaderText("Scopo della lezione");
		result.setContentText(message);
		result.show();
	}

	public void loadMethod(String methodName, String lineNumber) {
		pseudocodeController.showMethod(methodName, Integer.parseInt(lineNumber));
	}

	
}
