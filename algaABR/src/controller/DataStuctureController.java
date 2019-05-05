package controller;

public abstract class DataStuctureController {

	/* lesson controller per comunicare con gli altri controllers*/
	protected LessonController lessonController;

	protected void setLessonController(LessonController lc) {
		this.lessonController = lc;
	}
	

}
