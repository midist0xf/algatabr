package controller;

import javafx.fxml.FXML;

public class LessonController extends NavigationController {
	
	private int lessonNumber;
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;
	
	public void setLesson(String lessonName) {
		this.lessonNumber = Integer.valueOf(lessonName.substring(8));
		questionController.loadQuestions(lessonNumber);
	}
}
