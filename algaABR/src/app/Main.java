package app;


import org.json.simple.JSONObject;

import controller.LessonController;
import controller.MenuController;
import controller.NavigationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("AlgaT - ABR");
			
			gotoMenu();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * This method loads the view of a specific lesson.
	 * @param lessonName the name of the lesson to load
	 * @param jsonRoot json object which contains info about the lesson
	 */
	public void gotoLesson(String lessonName, JSONObject jsonRoot) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LessonView.fxml"));
			Parent root = loader.load();
			
			LessonController controller = loader.getController();
			controller.setMainApp(this);
			controller.setLesson(lessonName, jsonRoot);
			
			Scene scene = new Scene(root, 1150, 500);
			scene.getStylesheets().add("/view/style.css");
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method loads the menu view.
	 */
	public void gotoMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
			Parent root = loader.load();
			
			MenuController controller = loader.getController();
			controller.setMainApp(this);
			controller.loadCBoxLessons();
			
			Scene scene = new Scene(root, 1150, 500);
			scene.getStylesheets().add("/view/style.css");
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
