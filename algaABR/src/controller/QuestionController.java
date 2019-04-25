package controller;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

/*
 * Ci devono essere almeno 5 domande per lezione
 * per adesso facciamo che ogni domanda ha 3 risposte
 * 
 * la struttura dati che contiene le domande/risposte e'
 * un array di vettori di stringhe. e' organizzato in questo
 * modo:
 * 
 * [ 
 * 	[domanda1, rispostagiusta, spiegazgiusta, rispostasbagliata, spiegazionesbagl, rispostasbagliata, spiegazionesbagl]
 * 	[domanda2, rispostagiusta, spiegazgiusta, rispostasbagliata, spiegazionesbagl, rispostasbagliata, spiegazionesbagl]
 * ]
 * 
 */

public class QuestionController extends NavigationController{
	
	private String[][] questAnsExp;
	private int currentQuestion = 0;
	
	@FXML Text questionText;
	@FXML RadioButton firstAnswerRadioB;
	@FXML RadioButton secondAnswerRadioB;
	@FXML RadioButton thirdAnswerRadioB;
	
	@FXML
	public void handleAnswerConfirm() {
		Alert result = new Alert(Alert.AlertType.INFORMATION);
		String selectedAnswer = getSelectedAnswer();
		
		if (selectedAnswer == "") {
			// Ramo nessuna risposta selezionata
			result.setTitle("Attenzione");
			result.setContentText("Nessuna risposta selezionata");
			result.show();
		} else if (selectedAnswer != questAnsExp[currentQuestion][1]) {
			// Ramo risposta sbagliata
			result.setTitle("Risposta errata");
			result.setHeaderText("Riprova");

			// carica la spiegazione associata alla risposta errata
			if (selectedAnswer == questAnsExp[currentQuestion][3])
				result.setContentText(questAnsExp[currentQuestion][4]);
			else
				result.setContentText(questAnsExp[currentQuestion][6]);

			result.show();
		} else {
			// Ramo risposta giusta
			
			// controllo se esiste una domanda successiva con trycatch
			// nel caso non esistesse la funzione showQuestion lancia 
			// una eccezione outOfBounds
			try {
				currentQuestion++;
				showQuestion();

				result.setTitle("Complimenti");
				result.setHeaderText("Risposta corretta");
				result.setContentText(questAnsExp[currentQuestion][2]);
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
	
	private String getSelectedAnswer() {
		if (firstAnswerRadioB.isSelected())
			return firstAnswerRadioB.getText();
		if (secondAnswerRadioB.isSelected())
			return secondAnswerRadioB.getText();
		if (thirdAnswerRadioB.isSelected())
			return thirdAnswerRadioB.getText();
		return "";
	}
	
	public void loadQuestions(JSONObject jLesson) {
		try {
			Set questions = jLesson.keySet();

			questAnsExp = new String[questions.size()][7]; // perche 7? guarda spiegazione all inizio

			Iterator qIter = questions.iterator();	

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

	private void showQuestion() {
		questionText.setText(questAnsExp[this.currentQuestion][0]);
		
		// randomizza la posizione delle risposte, carica in una lista
		// carica in una lista gli _indici_ delle risposte
		List<Integer> qPicker = new ArrayList<Integer>();
		qPicker.add(1);
		qPicker.add(3);
		qPicker.add(5);
		java.util.Collections.shuffle(qPicker);

		firstAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(0)]);
		secondAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(1)]);	
		thirdAnswerRadioB.setText(questAnsExp[this.currentQuestion][qPicker.get(2)]);
		
		// deseleziona tutti i radiobox (utile quando si passa alla domanda successiva)
		firstAnswerRadioB.setSelected(false);
		secondAnswerRadioB.setSelected(false);
		thirdAnswerRadioB.setSelected(false);
	}
	
}

