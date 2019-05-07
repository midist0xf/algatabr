package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;



public class QuestionController extends NavigationController{
	
	/**
	 * The data structure which contains questions and answers is an array of string arrays
	 * and is filled as follows:
	 * [ 
	 * 	[question1, correctanswer, explanationcorrectans, wronganswer, explanationwrongans, wronganswer, explanationwrongans]
	 * 	[question2, correctanswer, explanationcorrectans, wronganswer, explanationwrongans, wronganswer, explanationwrongans]
	 * ]
	 */
	private String[][] questAnsExp;
	private int currentQuestion = 0;
	
	@FXML Text questionText;
	@FXML RadioButton firstAnswerRadioB;
	@FXML RadioButton secondAnswerRadioB;
	@FXML RadioButton thirdAnswerRadioB;
	
	
	/**
	 * Checks if the answer chosen by the user is correct. 
	 * If the answer is wrong shows the associated explanation. 
	 */
	@FXML
	public void handleAnswerConfirm() {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		String selectedAnswer = getSelectedAnswer();
		
		if (selectedAnswer == "") {
			// No answer selected
			result.setTitle("Attenzione");
			result.setContentText("Nessuna risposta selezionata");
			result.show();
		} else if (selectedAnswer != questAnsExp[currentQuestion][1]) {
			// Wrong answer
			result.setTitle("Risposta errata");
			result.setHeaderText("Riprova");

			// Shows the explanation associated to the answer 
			if (selectedAnswer == questAnsExp[currentQuestion][3])
				result.setContentText(questAnsExp[currentQuestion][4]);
			else
				result.setContentText(questAnsExp[currentQuestion][6]);

			result.show();
		} else {
			// Correct answer 
			
			// Show next question if exists, throws an outOfBounds exception
			// and shows the menu otherwise
			try {
				currentQuestion++;
				showQuestion();

				result.setTitle("Complimenti");
				result.setHeaderText("Risposta corretta");
				result.setContentText(questAnsExp[currentQuestion-1][2]);
				result.show();

			} catch (IndexOutOfBoundsException e) {
				result.setTitle("Complimenti");
				result.setHeaderText("Hai completato la lezione");
				result.setContentText(questAnsExp[currentQuestion-1][2]);
				result.show();
				mainApp.gotoMenu();
			}
		}
	}
	
	/**
	 * Gets the text of the answer selected by the user
	 * @return a string with the text of the chosen answer 
	 * if one of the radioboxes is selected, empty string otherwise
	 */
	private String getSelectedAnswer() {
		if (firstAnswerRadioB.isSelected())
			return firstAnswerRadioB.getText();
		if (secondAnswerRadioB.isSelected())
			return secondAnswerRadioB.getText();
		if (thirdAnswerRadioB.isSelected())
			return thirdAnswerRadioB.getText();
		return "";
	}
	
	/**
	 * Given a json object, which represents a lesson's questions/answers/explanations, 
	 * loads the questions/answers/explanations within the dedicated data structure
	 * @param jLesson JsonObject with  question/answers/explanations of the lesson 
	 */
	public void loadQuestions(JSONObject jLesson) {
		try {
			// Get the list of questions associated with the lesson passed as argument
			Set questions = jLesson.keySet();

			// 7 because a record is [question1, correctanswer, explanationcorrectans, wronganswer, explanationwrongans, wronganswer, explanationwrongans]
			questAnsExp = new String[questions.size()][7];
			
			Iterator qIter = questions.iterator();	
			
			// For each question stores answers and explanations in the appropriated array 
			for (int i = 0; qIter.hasNext(); i++) {
				String current = (String) qIter.next();

				questAnsExp[i][0] = current;

				JSONArray answersExplanations = (JSONArray) jLesson.get(current);

				for (int j = 1; j < 7; j++) {
					questAnsExp[i][j] = answersExplanations.get(j-1).toString();
				}
			}
			
			this.currentQuestion = 0;
			showQuestion();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the current question and the associated answers options
	 */
	private void showQuestion() {
		questionText.setText(questAnsExp[this.currentQuestion][0]);
		
		// Randomizes answers positions
		// Stores the indexes of correct answers in a list 
		List<Integer> qPicker = new ArrayList<Integer>();
		qPicker.add(1);
		qPicker.add(3);
		qPicker.add(5);
		java.util.Collections.shuffle(qPicker);

		firstAnswerRadioB.setWrapText(true);		
		secondAnswerRadioB.setWrapText(true);		
		thirdAnswerRadioB.setWrapText(true);

		// Sets the text for each radio button
		firstAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(0)]);
		secondAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(1)]);	
		thirdAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(2)]);
		
		// Unchecks radioboxes
		firstAnswerRadioB.setSelected(false);
		secondAnswerRadioB.setSelected(false);
		thirdAnswerRadioB.setSelected(false);
	}
	
}

