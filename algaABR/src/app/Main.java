package app;


import controller.Controller;
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
			
			changeScene("/view/MenuView.fxml");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void changeScene(String fxmlResource) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlResource));
			Parent root = loader.load();
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
			
			this.primaryStage.setScene(new Scene(root, 700, 500));
			this.primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
