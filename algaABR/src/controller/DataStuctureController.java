package controller;

public abstract class DataStuctureController {

	/* lesson controller to communicate with others controllers */
	protected LessonController lessonController;
	
	protected String initialSteps;

	protected void setLessonController(LessonController lc) {
		this.lessonController = lc;
	}

	public void setInitialSteps(String stepsNotParsed) {
		initialSteps = stepsNotParsed;
	}
	

}
