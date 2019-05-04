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
	
	public void gotoLesson(String lessonName, JSONObject jsonRoot) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LessonView.fxml"));
			Parent root = loader.load();
			
			LessonController controller = loader.getController();
			controller.setMainApp(this);
			controller.setLesson(lessonName, jsonRoot);
			
			this.primaryStage.setScene(new Scene(root, 1150, 500));
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gotoMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MenuView.fxml"));
			Parent root = loader.load();
			
			MenuController controller = loader.getController();
			controller.setMainApp(this);
			controller.loadCBoxLessons();
			
			this.primaryStage.setScene(new Scene(root, 1150, 500));
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
