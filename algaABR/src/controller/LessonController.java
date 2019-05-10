package controller;

import java.io.IOException;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class LessonController extends NavigationController {
	
	@FXML private PseudocodeController pseudocodeController;
	@FXML private QuestionController questionController;
	@FXML private Pane dataStructurePane;

	private DataStuctureController datastructureController;

	
	/**
	 * Communicates to the questionController and to the pseudocodeController
	 * which questions and which pseudocode to show.
	 * Loads the appropriate view accordingly to the lesson.
	 * @param lessonName a string which identifies a single lesson
	 * @param jsonRoot a json object which contains info on the lesson
	 */
	public void setLesson(String lessonName, JSONObject jsonRoot) {
		JSONObject selectedLesson = (JSONObject) jsonRoot.get(lessonName);
		
		showGreetingMessage(selectedLesson.get("DescrizioneLezione").toString());

		questionController.loadQuestions((JSONObject) selectedLesson.get("DomandeRiposteSpiegazioni"));
		questionController.setMainApp(this.mainApp);
		
		pseudocodeController.loadCodes((JSONObject) selectedLesson.get("Pseudocodice"));
		
		// dynamically loads the algorithm/interactive data structure
		switch (selectedLesson.get("AlgoritmoInterattivo").toString()) {
		case "ABR":
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ABRView.fxml"));
				dataStructurePane.getChildren().add(loader.load());
				datastructureController = loader.getController();

				ABRViewController a = (ABRViewController) datastructureController;
				a.setInitialSteps(selectedLesson.get("Steps").toString());
				a.handleInitialSteps();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "RB":
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RBView.fxml"));
				dataStructurePane.getChildren().add(loader.load());
				datastructureController = loader.getController();

				RBViewController a = (RBViewController) datastructureController;
				a.setInitialSteps(selectedLesson.get("Steps").toString());
				a.handleInitialSteps();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			// controller not found
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

	/**
	 * Shows an alert which describes the lesson.
	 * @param message the description of the lesson to show in the alert
	 */
	private void showGreetingMessage(String message) {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		result.setTitle("Benvenuto");
		result.setHeaderText("Scopo della lezione");
		result.setContentText(message);
		result.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

		result.show();
	}

	/**
	 * Communicates to the pseudocodeController which method to show and which line to highlight.
	 * @param methodName the name of the method to show
	 * @param lineNumber the number of the line of the pseudocode to highlight
	 */
	public void loadMethod(String methodName, String lineNumber) {
		pseudocodeController.showMethod(methodName, Integer.parseInt(lineNumber));
	}

	
}
