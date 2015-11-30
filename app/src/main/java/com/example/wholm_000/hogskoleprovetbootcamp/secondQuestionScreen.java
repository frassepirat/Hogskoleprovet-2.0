package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Wholm_000 on 2015-11-18.
 */
public class secondQuestionScreen extends AppCompatActivity {

    private String questionNum = "1";
    String[][] answers;
    int[] orderOfQuestions;
    int[] questionChoices;
    private int[] correctAnsPos;
    String mode = "xyz";
    int numOfQuestions = 8;
    String screen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_question);

        Intent intent = getIntent();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        questionNum = intent.getExtras().getString("questionNumber");

        Bundle bundle = intent.getExtras().getBundle("answerBundle");
        answers = (String[][]) bundle.getSerializable("answersArray");
        questionChoices = intent.getExtras().getIntArray("questionChoices");
        correctAnsPos = intent.getIntArrayExtra("correctAnswers");
        mode = intent.getStringExtra("mode");

        numOfQuestions = intent.getIntExtra("numberOfQuestions", 0);

        if(Integer.parseInt(questionNum) >= numOfQuestions && intent.getStringExtra("screen").equals("score")){
            Button finishButton = (Button) findViewById(R.id.nextQuestion);
            finishButton.setText("Avsluta");
        }
        //finish Quiz.
        if(intent.getStringExtra("screen") != null){
            screen = intent.getStringExtra("screen");
            orderOfQuestions = intent.getIntArrayExtra("orderArray");

            RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioB4);

            switch (questionChoices[Integer.parseInt(questionNum)]){
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
                case 4:
                    rb4.setChecked(true);
                    break;
                default:
                    break;
            }
            setImageToMode();
            setQuestionsAndAnswers();
        } else if (Integer.parseInt(questionNum) > numOfQuestions) {

            Intent goToStartScreen = new Intent(this, kvantitativDel.class);

            int numOfCorrect = 0;

            for(int i = 0; i < numOfQuestions; i++){
                if(questionChoices[i + 1] == correctAnsPos[i]){
                    numOfCorrect++;
                }
            }
            goToStartScreen.putExtra("numQuestions", numOfQuestions);
            goToStartScreen.putExtra("correctAnswers", numOfCorrect);

            startActivity(goToStartScreen);

            finish();

        } else {

            TextView displayScore = (TextView) findViewById(R.id.warningText2);

            orderOfQuestions = intent.getExtras().getIntArray("orderArray");

            RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioB4);

            setQuestionsAndAnswers();

            switch (questionChoices[Integer.parseInt(questionNum)]) {
                case 1:
                    rb1.setChecked(true);
                    break;
                case 2:
                    rb2.setChecked(true);
                    break;
                case 3:
                    rb3.setChecked(true);
                    break;
                case 4:
                    rb4.setChecked(true);
                    break;
                default:
                    break;
            }
            setImageToMode();
        }

    }

    public void nextQuestion(View view) {

        RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioB4);
        TextView warningText = (TextView) findViewById(R.id.warningText2);

        int queNum = Integer.parseInt(questionNum);

        if(screen.equals("score") && Integer.parseInt(questionNum) == numOfQuestions){
            Intent goToStartScreen = new Intent(this, kvantitativDel.class);

            int numOfCorrect = 0;

            for(int i = 0; i < numOfQuestions; i++){
                if(questionChoices[i + 1] == correctAnsPos[i]){
                    numOfCorrect++;
                }
            }
            goToStartScreen.putExtra("numQuestions", numOfQuestions);
            goToStartScreen.putExtra("correctAnswers", numOfCorrect);

            startActivity(goToStartScreen);

            finish();
        } else if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

            if(rb1.isChecked()){
                questionChoices[queNum] = 1;
            } else if(rb2.isChecked()) {
                questionChoices[queNum] = 2;
            } else if(rb3.isChecked()){
                questionChoices[queNum] = 3;
            } else if(rb4.isChecked()){
                questionChoices[queNum] = 4;
            }

            Intent goingBack = new Intent();

            goingBack.putExtra("questionNumber", Integer.parseInt(questionNum) + 1);
            goingBack.putExtra("questionChoices", questionChoices);
            goingBack.putExtra("normalNext", true);

            goingBack.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            setResult(RESULT_OK, goingBack);

            overridePendingTransition(0, 0);

            finish();
        } else{
            warningText.setText(R.string.warningText);
        }

    }

    public void gotoNotes(View view) {
        Intent goToNotes = new Intent(this, notes.class);

        int result = 1;

        Bundle bundle = new Bundle();

        bundle.putSerializable("answersArray", answers);
        goToNotes.putExtra("answerBundle", bundle);
        goToNotes.putExtra("orderOfQuestions", orderOfQuestions);
        goToNotes.putExtra("questionNumber", Integer.parseInt(questionNum));
        goToNotes.putExtra("mode", mode);


        startActivityForResult(goToNotes, result);

    }
    public void getToAbout(View view) {
        Intent goToNotes = new Intent(this, aboutQuestion.class);

        int result = 1;

        Bundle bundle = new Bundle();

        bundle.putSerializable("answersArray", answers);
        goToNotes.putExtra("answerBundle", bundle);
        goToNotes.putExtra("orderOfQuestions", orderOfQuestions);
        goToNotes.putExtra("questionNumber", Integer.parseInt(questionNum));
        goToNotes.putExtra("mode", mode);

        startActivityForResult(goToNotes, result);
    }

    public void setQuestionsAndAnswers(){

        RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioB4);

        TextView setQuestion = (TextView) findViewById(R.id.question);

        TextView setQuestionNumber = (TextView) findViewById(R.id.questionNumber2);

        int queNumber = Integer.parseInt(questionNum);

        setQuestionNumber.setText(queNumber + " / " + numOfQuestions);

        if(mode.equals("kva")) {
            setQuestion.setText(Integer.toString(queNumber) + ". " + answers[orderOfQuestions[queNumber - 1]][0]
                    + "\n\n" + answers[orderOfQuestions[queNumber - 1]][1]
                    + "\n\n" + answers[orderOfQuestions[queNumber - 1]][2]);

            rb1.setText("A" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][3]);
            rb2.setText("B" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][4]);
            rb3.setText("C" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][5]);
            rb4.setText("D" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][6]);
        } else if(mode.equals("xyz")){
            setQuestion.setText(Integer.toString(queNumber) + ". " + answers[orderOfQuestions[queNumber - 1]][0]);

            rb1.setText("A" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][1]);
            rb2.setText("B" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][2]);
            rb3.setText("C" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][3]);
            rb4.setText("D" + getString(R.string.spaces) + answers[orderOfQuestions[queNumber - 1]][4]);
        }
        if(screen.equals("score")){

            //clear background and text
            rb1.setBackgroundColor(0);
            rb2.setBackgroundColor(0);
            rb3.setBackgroundColor(0);
            rb4.setBackgroundColor(0);

            rb1.setTextColor(Color.WHITE);
            rb2.setTextColor(Color.WHITE);
            rb3.setTextColor(Color.WHITE);
            rb4.setTextColor(Color.WHITE);

            if(mode.equals("kva")){
                if(rb1.isChecked()){
                    rb1.setBackgroundColor(Color.RED);
                    rb1.setTextColor(Color.BLACK);
                } else if(rb2.isChecked()){
                    rb2.setBackgroundColor(Color.RED);
                    rb2.setTextColor(Color.BLACK);
                } else if(rb3.isChecked()){
                    rb3.setBackgroundColor(Color.RED);
                    rb3.setTextColor(Color.BLACK);
                } else if(rb4.isChecked()){
                    rb4.setBackgroundColor(Color.RED);
                    rb4.setTextColor(Color.BLACK);
                }
                String que = answers[orderOfQuestions[queNumber - 1]][7];

                String ans1 = answers[orderOfQuestions[queNumber - 1]][3];
                String ans2 = answers[orderOfQuestions[queNumber - 1]][4];
                String ans3 = answers[orderOfQuestions[queNumber - 1]][5];
                String ans4 = answers[orderOfQuestions[queNumber - 1]][6];

                if(ans1.equals(que)){
                    rb1.setBackgroundColor(Color.GREEN);
                    rb1.setTextColor(Color.BLACK);
                } else if(ans2.equals(que)){
                    rb2.setBackgroundColor(Color.GREEN);
                    rb2.setTextColor(Color.BLACK);
                } else if(ans3.equals(que)){
                    rb3.setBackgroundColor(Color.GREEN);
                    rb3.setTextColor(Color.BLACK);
                } else if(ans4.equals(que)){
                    rb4.setBackgroundColor(Color.GREEN);
                    rb4.setTextColor(Color.BLACK);
                }
                rb1.setClickable(false);
                rb2.setClickable(false);
                rb3.setClickable(false);
                rb4.setClickable(false);
            } else if(mode.equals("xyz")){

                if(rb1.isChecked()){
                    rb1.setBackgroundColor(Color.RED);
                    rb1.setTextColor(Color.BLACK);
                } else if(rb2.isChecked()){
                    rb2.setBackgroundColor(Color.RED);
                    rb2.setTextColor(Color.BLACK);
                } else if(rb3.isChecked()){
                    rb3.setBackgroundColor(Color.RED);
                    rb3.setTextColor(Color.BLACK);
                } else if(rb4.isChecked()){
                    rb4.setBackgroundColor(Color.RED);
                    rb4.setTextColor(Color.BLACK);
                }

                String que = answers[orderOfQuestions[queNumber - 1]][5];

                String ans1 = answers[orderOfQuestions[queNumber - 1]][1];
                String ans2 = answers[orderOfQuestions[queNumber - 1]][2];
                String ans3 = answers[orderOfQuestions[queNumber - 1]][3];
                String ans4 = answers[orderOfQuestions[queNumber - 1]][4];

                if(ans1.equals(que)){
                    rb1.setBackgroundColor(Color.GREEN);
                    rb1.setTextColor(Color.BLACK);
                } else if(ans2.equals(que)){
                    rb2.setBackgroundColor(Color.GREEN);
                    rb2.setTextColor(Color.BLACK);
                } else if(ans3.equals(que)){
                    rb3.setBackgroundColor(Color.GREEN);
                    rb3.setTextColor(Color.BLACK);
                } else if(ans4.equals(que)){
                    rb4.setBackgroundColor(Color.GREEN);
                    rb4.setTextColor(Color.BLACK);
                }
                rb1.setClickable(false);
                rb2.setClickable(false);
                rb3.setClickable(false);
                rb4.setClickable(false);
            }
        }
    }
    private void setImageToMode() {
        ImageView img = (ImageView) findViewById(R.id.imageView);

        int queNumber = Integer.parseInt(questionNum);

        if(mode.equals("xyz")) {
            if (answers[orderOfQuestions[queNumber - 1]][6].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[queNumber - 1]][7], "drawable", getPackageName()));
            } else {
                img.setBackgroundResource(0);
            }
        } else if(mode.equals("kva")){
            if (answers[orderOfQuestions[queNumber - 1]][8].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[queNumber - 1]][9], "drawable", getPackageName()));
            } else {
                img.setBackgroundResource(0);
            }
        }
    }


    public void goToPreviousQuestion(View view) {

        RadioButton rb1 = (RadioButton) findViewById(R.id.radioB1);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioB2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioB3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioB4);

        int queNum = Integer.parseInt(questionNum);

        if(rb1.isChecked()){
            questionChoices[queNum] = 1;
        } else if(rb2.isChecked()) {
            questionChoices[queNum] = 2;
        } else if(rb3.isChecked()){
            questionChoices[queNum] = 3;
        } else if(rb4.isChecked()){
            questionChoices[queNum] = 4;
        }

        Intent goingBack = new Intent();

        goingBack.putExtra("questionNumber", Integer.parseInt(questionNum) - 1);
        goingBack.putExtra("questionChoices", questionChoices);
        goingBack.putExtra("loadLastQuestion", true);

        setResult(RESULT_OK, goingBack);

        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                this.finish();
                Intent intent = new Intent(this, kvantitativDel.class);

                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
