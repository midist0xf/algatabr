package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class LessonController extends NavigationController {
	
	private int lessonNumber;
	
	@FXML 
	private PseudocodeController pseudocodeController;
	
	public void initialize() {
		pseudocodeController.lessonNumber = 2;
	}
	
	public void setLesson(String lessonName) {
		this.lessonNumber = Integer.valueOf(lessonName.substring(8));
	}
}
