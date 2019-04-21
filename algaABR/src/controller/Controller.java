package controller;

import app.Main;

public abstract class Controller {
	
	protected Main mainApp;

	public void setMainApp(Main main) {
		this.mainApp = main;
	}

}
