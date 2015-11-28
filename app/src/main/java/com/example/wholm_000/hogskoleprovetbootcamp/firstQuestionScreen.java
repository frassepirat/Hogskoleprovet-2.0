package com.example.wholm_000.hogskoleprovetbootcamp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class firstQuestionScreen extends AppCompatActivity {


    private int queNumber = 1;
    private String[][] answers = new String[10][8];
    private int numOfQuestions = 0;
    private int[] orderOfQuestions;
    private int[] questionChoices;
    private int[] correctAnsPos;
    private String mode = "xyz";
    private String screen = "play";
    private boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_question_screen);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        InputStream is = getResources().openRawResource(R.raw.xyzquestions);

        int numLinesInTxt = 8;
        int minAnsRange = 1;
        int maxAnsRange = 5;

        mode = intent.getStringExtra("mode");

        if(intent.getStringExtra("screen") != null){
            screen = intent.getStringExtra("screen");
            questionChoices = intent.getIntArrayExtra("questionChoices");
            queNumber = intent.getIntExtra("questionNumber", 1);
            Bundle bundle = intent.getExtras().getBundle("answerBundle");
            answers = (String[][]) bundle.getSerializable("answersArray");
            orderOfQuestions = intent.getIntArrayExtra("orderArray");
            numOfQuestions = intent.getIntExtra("numberOfQuestions", 0);
            correctAnsPos = intent.getIntArrayExtra("correctAnswers");


            RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);

            if(queNumber >= numOfQuestions && intent.getStringExtra("screen").equals("score")){
                Button finishButton = (Button) findViewById(R.id.nextButton);
                finishButton.setText("Avsluta");
            }

            switch (questionChoices[queNumber]){
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
            setQuestionsAndAnswers();
            //setImageToMode();
        } else {

            if (mode.equals("xyz")) {
                is = getResources().openRawResource(R.raw.xyzquestions);
            } else if (mode.equals("kva")) {
                answers = new String[10][10];
                numLinesInTxt = 10;
                minAnsRange = 3;
                maxAnsRange = 7;
                is = getResources().openRawResource(R.raw.kvaquestions);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder out = new StringBuilder();
            String line;

            try {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < numLinesInTxt; j++) {
                        answers[i][j] = reader.readLine();
                    }
                }

                //get the number of questions.
                while (answers[numOfQuestions][0] != null) {
                    numOfQuestions++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //shuffle the order that the questions are asked in.
            orderOfQuestions = new int[numOfQuestions];
            // + 1 because it's 1 indexed. { 0, 3, 2, 3 . . . }
            questionChoices = new int[numOfQuestions + 1];

            List<Integer> shuffleIntBuffer = new ArrayList<>();

            for (int i = 0; i < numOfQuestions; i++) {
                shuffleIntBuffer.add(i);
                orderOfQuestions[i] = i;
            }

            Collections.shuffle(shuffleIntBuffer);

            for (int i = 0; i < numOfQuestions; i++) {
                orderOfQuestions[i] = shuffleIntBuffer.get(i);
            }

            //shuffle the answerpositions
            List<String> shuffleBuffer = new ArrayList<>();
            for (int i = 0; i < numOfQuestions; i++) {
                for (int j = minAnsRange; j < maxAnsRange; j++) {
                    shuffleBuffer.add(answers[i][j]);
                }
                Collections.shuffle(shuffleBuffer);
                for (int j = 0; j < 4; j++) {
                    answers[i][j + minAnsRange] = shuffleBuffer.get(j);
                }
                shuffleBuffer.clear();

            }

            //get the correct position of the answer
            correctAnsPos = new int[numOfQuestions];

            for (int i = 0; i < numOfQuestions; i++) {
                for (int j = minAnsRange; j < maxAnsRange; j++) {
                    int x = orderOfQuestions[i];
                    if (answers[x][j].equals(answers[x][maxAnsRange])) {
                        correctAnsPos[i] = j;
                        break;
                    }
                }
            }

            if(mode.equals("kva")){
                for(int i = 0; i < correctAnsPos.length; i++){
                    correctAnsPos[i] -= 2;
                }
            }

        }
        setQuestionsAndAnswers();

        if(queNumber == 1) {
            Button myButton = (Button) findViewById(R.id.previousButton);

            myButton.setEnabled(false);
        }

        //set image if it exists
        setImageToMode();


    }



    public void moveOn(View view)    {

        //check if correct
        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);

        TextView warningText = (TextView) findViewById(R.id.warningText);

        if(screen.equals("score") && queNumber == numOfQuestions){
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
        } else if(screen.equals("score")){
            Intent checkScore = new Intent(this,
                    secondQuestionScreen.class);

            final int result = 1;

            String buffer = Integer.toString(queNumber + 1);

            checkScore.putExtra("questionNumber", buffer);
            checkScore.putExtra("numberOfQuestions", numOfQuestions);
            checkScore.putExtra("questionChoices", questionChoices);
            checkScore.putExtra("correctAnswers", correctAnsPos);
            checkScore.putExtra("loadLastQuestion", true);
            checkScore.putExtra("numberOfQuestions", numOfQuestions);
            Bundle bundle = new Bundle();
            bundle.putSerializable("answersArray", answers);
            checkScore.putExtra("answerBundle", bundle);
            checkScore.putExtra("orderArray", orderOfQuestions);
            checkScore.putExtra("mode", mode);
            checkScore.putExtra("screen", "score");


            startActivityForResult(checkScore, result);


        } else if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {

            if(rb1.isChecked()){
                questionChoices[queNumber] = 1;
            } else if(rb2.isChecked()) {
                questionChoices[queNumber] = 2;
            } else if(rb3.isChecked()){
                questionChoices[queNumber] = 3;
            } else if(rb4.isChecked()){
                questionChoices[queNumber] = 4;
            }


            //change screen
            Intent nextQuestion = new Intent(this,
                    secondQuestionScreen.class);

            final int result = 1;

            String buffer = Integer.toString(queNumber + 1);
            Bundle bundle = new Bundle();

            bundle.putSerializable("answersArray", answers);
            nextQuestion.putExtra("answerBundle", bundle);
            nextQuestion.putExtra("orderArray", orderOfQuestions);
            nextQuestion.putExtra("questionNumber", buffer);
            nextQuestion.putExtra("numberOfQuestions", numOfQuestions);
            nextQuestion.putExtra("questionChoices", questionChoices);
            nextQuestion.putExtra("correctAnswers", correctAnsPos);
            nextQuestion.putExtra("mode", mode);
            nextQuestion.putExtra("screen", screen);
            if(screen.equals("score")){
                nextQuestion.putExtra("loadLastQuestion", true);
            }

            warningText.setText("");

            startActivityForResult(nextQuestion, result);

            RadioGroup group = (RadioGroup) findViewById(R.id.group);
            group.clearCheck();


        } else{
            warningText.setText(R.string.warningText);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null) {
            TextView numberDisplay = (TextView) findViewById(R.id.questionNumber);
            if(data.getBooleanExtra("normalNext", false)) {
                super.onActivityResult(requestCode, resultCode, data);

                queNumber = data.getIntExtra("questionNumber", 0);
                questionChoices = data.getIntArrayExtra("questionChoices");

                if (queNumber > numOfQuestions && finish == true) {
                    Intent goToStartScreen = new Intent(this, kvantitativDel.class);

                    int numOfCorrect = 0;

                    for(int i = 0; i < numOfQuestions; i++){
                        if(questionChoices[i + 1] == correctAnsPos[i]){
                            numOfCorrect++;
                        }
                    }
                    goToStartScreen.putExtra("numQuestions", numOfQuestions);
                    goToStartScreen.putExtra("correctAnswers", numOfCorrect);

                    setResult(RESULT_OK, goToStartScreen);

                    finish();
                }else if(queNumber > numOfQuestions) {
                    Intent checkScore = new Intent(this, firstQuestionScreen.class);

                    checkScore.putExtra("questionNumber", Integer.toString(queNumber - 1));
                    checkScore.putExtra("numberOfQuestions", numOfQuestions);
                    checkScore.putExtra("questionChoices", questionChoices);
                    checkScore.putExtra("loadLastQuestion", true);
                    checkScore.putExtra("numberOfQuestions", numOfQuestions);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("answersArray", answers);
                    checkScore.putExtra("answerBundle", bundle);
                    checkScore.putExtra("orderArray", orderOfQuestions);
                    checkScore.putExtra("correctAnswers", correctAnsPos);
                    checkScore.putExtra("mode", mode);
                    checkScore.putExtra("screen", "score");

                    startActivity(checkScore);

                    finish();
                }else {


                    RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
                    RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
                    RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
                    RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);



                    switch (questionChoices[queNumber]){
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

                    setQuestionsAndAnswers();

                    setImageToMode();


                }
            } else if(data.getBooleanExtra("loadLastQuestion", false)){
                questionChoices = data.getIntArrayExtra("questionChoices");
                queNumber = data.getIntExtra("questionNumber", 1);

                RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
                RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
                RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
                RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);

                switch (questionChoices[queNumber]){
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

                setQuestionsAndAnswers();
                setImageToMode();

            }

            Button prevButton = (Button) findViewById(R.id.previousButton);

            if(queNumber == 1){
                prevButton.setEnabled(false);
            } else{
                prevButton.setEnabled(true);
            }

            Button nextButton = (Button) findViewById(R.id.nextButton);

            if(queNumber == numOfQuestions){
                nextButton.setText("Avsluta");
            } else{
                nextButton.setText("Nästa");
            }
        }
    }

    public void getToNotes(View view) {
        Intent goToNotes = new Intent(this, notes.class);

        int result = 1;

        Bundle bundle = new Bundle();

        bundle.putSerializable("answersArray", answers);
        goToNotes.putExtra("answerBundle", bundle);
        goToNotes.putExtra("orderOfQuestions", orderOfQuestions);
        goToNotes.putExtra("questionNumber", queNumber);
        goToNotes.putExtra("mode", mode);

        startActivityForResult(goToNotes, result);
    }

    public void prevQuestion(View view) {


        if(screen.equals("score")){
            Intent checkScore = new Intent(this,
                    secondQuestionScreen.class);

            final int result = 1;

            String buffer = Integer.toString(queNumber - 1);

            checkScore.putExtra("questionNumber", buffer);
            checkScore.putExtra("numberOfQuestions", numOfQuestions);
            checkScore.putExtra("questionChoices", questionChoices);
            checkScore.putExtra("loadLastQuestion", true);
            checkScore.putExtra("numberOfQuestions", numOfQuestions);
            Bundle bundle = new Bundle();
            bundle.putSerializable("answersArray", answers);
            checkScore.putExtra("answerBundle", bundle);
            checkScore.putExtra("orderArray", orderOfQuestions);
            checkScore.putExtra("mode", mode);
            checkScore.putExtra("screen", "score");


            startActivityForResult(checkScore, result);


        } else {
            Intent goingBack = new Intent(this, secondQuestionScreen.class);

            RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
            RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
            RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
            RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);

            if (rb1.isChecked()) {
                questionChoices[queNumber] = 1;
            } else if (rb2.isChecked()) {
                questionChoices[queNumber] = 2;
            } else if (rb3.isChecked()) {
                questionChoices[queNumber] = 3;
            } else if (rb4.isChecked()) {
                questionChoices[queNumber] = 4;
            }

            goingBack.putExtra("questionNumber", Integer.toString(queNumber - 1));
            goingBack.putExtra("numberOfQuestions", numOfQuestions);
            goingBack.putExtra("questionChoices", questionChoices);
            goingBack.putExtra("loadLastQuestion", true);
            goingBack.putExtra("numberOfQuestions", numOfQuestions);
            Bundle bundle = new Bundle();
            bundle.putSerializable("answersArray", answers);
            goingBack.putExtra("answerBundle", bundle);
            goingBack.putExtra("loadLastQuestion", true);
            goingBack.putExtra("orderArray", orderOfQuestions);
            goingBack.putExtra("mode", mode);

            int result = 1;

            startActivityForResult(goingBack, result);
        }

    }

    private void setImageToMode() {
        ImageView img = (ImageView) findViewById(R.id.imageViewFirst);

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
    public void setQuestionsAndAnswers(){

        RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
        RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);

        TextView setQuestion = (TextView) findViewById(R.id.firstQuestion);

        TextView setQuestionNumber = (TextView) findViewById(R.id.questionNumber);
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


    public int[] shuffleArray(int[] array){

        Random rnd = new Random(System.currentTimeMillis());

        for(int i = 0; i < array.length; i++){
            int index = rnd.nextInt(i + 1);

            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }


        return array;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                Intent intent = new Intent(this, kvantitativDel.class);

                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
