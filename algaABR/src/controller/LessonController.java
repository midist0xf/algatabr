package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class LessonController extends NavigationController {
	
	private int lessonNumber;
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;
	
	public void initialize() {
		pseudocodeController.lessonNumber = 2;
		questionController.lessonNumber = 2;
	}
	
	public void setLesson(String lessonName) {
		this.lessonNumber = Integer.valueOf(lessonName.substring(8));
	}
}
