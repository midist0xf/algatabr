package controller;

import java.io.IOException;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

public class LessonController extends NavigationController {
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;
	@FXML private Pane dataStructurePane;

	private DataStuctureController datastructureController;

	public void setLesson(String lessonName, JSONObject jsonRoot) {
		JSONObject selectedLesson = (JSONObject) jsonRoot.get(lessonName);
		
		showGreetingMessage(selectedLesson.get("DescrizioneLezione").toString());

		questionController.loadQuestions((JSONObject) selectedLesson.get("DomandeRiposteSpiegazioni"));
		questionController.setMainApp(this.mainApp);
		
		pseudocodeController.loadCodes((JSONObject) selectedLesson.get("Pseudocodice"));
		
		// Carica dinamicamente l' algoritmo/struttura dati interattiva 
		switch (selectedLesson.get("AlgoritmoInterattivo").toString()) {
		case "ABR":
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ControlBarView.fxml"));
				dataStructurePane.getChildren().add(loader.load());
				datastructureController = loader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "RB":
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RBView.fxml"));
				dataStructurePane.getChildren().add(loader.load());
				datastructureController = loader.getController();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			// errore, controller not found
			Alert result = new Alert(Alert.AlertType.ERROR);
			result.setTitle("Errore");
			result.setHeaderText("Controller non trovato");
			result.setContentText("Struttura dati non implementata o file json non corretto");
			result.show();
			mainApp.gotoMenu();
			break;
		}

		datastructureController.setLessonController(this);
	}

	private void showGreetingMessage(String message) {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		result.setTitle("Benvenuto");
		result.setHeaderText("Scopo della lezione");
		result.setContentText(message);
		result.show();
	}

	public void loadMethod(String methodName, String lineNumber) {
		pseudocodeController.showMethod(methodName, Integer.parseInt(lineNumber));
	}

	
}
