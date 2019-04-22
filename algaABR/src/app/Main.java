package app;


import controller.LessonController;
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
			
			gotoLesson("Lezione 1");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void gotoLesson(String lessonName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LessonView.fxml"));
			Parent root = loader.load();
			
			LessonController controller = loader.getController();
			controller.setMainApp(this);
			controller.setLesson(lessonName);
			
			this.primaryStage.setScene(new Scene(root, 700, 500));
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void gotoMenu() {
		changeScene("/view/MenuView.fxml");
	}
	
	private void changeScene(String fxmlResource) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
			Parent root = loader.load();
			
			NavigationController controller = loader.getController();
			controller.setMainApp(this);
			
			this.primaryStage.setScene(new Scene(root, 700, 500));
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
