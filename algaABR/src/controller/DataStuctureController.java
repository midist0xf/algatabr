package controller;

public abstract class DataStuctureController {

	/* lesson controller to communicate with others controllers */
	protected LessonController lessonController;

	protected void setLessonController(LessonController lc) {
		this.lessonController = lc;
	}
	

}
