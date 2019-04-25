package controller;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;

public class LessonController extends NavigationController {
	
	private JSONObject jsonRoot;
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;

	public void setLesson(String lessonName, JSONObject jsonRoot) {
		this.jsonRoot = jsonRoot;
		
		JSONObject selectedLesson = (JSONObject) jsonRoot.get(lessonName);

		questionController.loadQuestions((JSONObject) selectedLesson.get("DomandeRiposteSpiegazioni"));
		questionController.setMainApp(this.mainApp);
		
		pseudocodeController.loadCodes((JSONObject) selectedLesson.get("Pseudocodice"));
	}

	
}
