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
 * 	[domanda1, rispostagiusta, rispostasbagliata, rispostasbagliata]
 * 	[domanda2, rispostagiusta, rispostasbagliata, rispostasbagliata]
 * ]
 * 
 */

public class QuestionController {
	
	private String[][] questionAnswers;
	private int currentQuestion = 0;
	
	@FXML Text questionText;
	@FXML RadioButton firstAnswerRadioB;
	@FXML RadioButton secondAnswerRadioB;
	@FXML RadioButton thirdAnswerRadioB;
	
	@FXML
	public void handleAnswerConfirm() {
	}
	
	public void loadQuestions(int lessonNumber) {
		try {
			Object parser = new JSONParser().parse(new FileReader("./questions.json"));
			JSONObject jo = (JSONObject) parser;
			
			JSONObject jLesson = (JSONObject) jo.get("Lesson" + lessonNumber);
			
			Set questions = jLesson.keySet();
			questionAnswers = new String[questions.size()][4];

			Iterator qIter = questions.iterator();
			for (int i = 0; qIter.hasNext(); i++) {
				String current = (String) qIter.next();

				questionAnswers[i][0] = current;
				JSONArray answers = (JSONArray) jLesson.get(current);

				for (int j = 1; j < 4; j++) {
					questionAnswers[i][j] = answers.get(j-1).toString();
				}
			}
			
			this.currentQuestion = 0;
			showQuestion();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showQuestion() {
		questionText.setText(questionAnswers[this.currentQuestion][0]);
		
		// randomizza la posizione delle risposte
		List<Integer> qPicker = new ArrayList<Integer>();
		qPicker.add(1);
		qPicker.add(2);
		qPicker.add(3);
		java.util.Collections.shuffle(qPicker);

		firstAnswerRadioB.setText(questionAnswers[this.currentQuestion][qPicker.get(0)]);
		secondAnswerRadioB.setText(questionAnswers[this.currentQuestion][qPicker.get(1)]);	
		thirdAnswerRadioB.setText(questionAnswers[this.currentQuestion][qPicker.get(2)]);
	}
	
}

