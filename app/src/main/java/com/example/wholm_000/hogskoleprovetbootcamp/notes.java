package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Wholm_000 on 2015-11-19.
 */
public class notes extends AppCompatActivity {

    String mode = "xyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_screen);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String[][] answers;

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras().getBundle("answerBundle");
        answers = (String[][]) bundle.getSerializable("answersArray");
        int questionNum = intent.getIntExtra("questionNumber", 0);
        int[] orderOfQuestions = intent.getIntArrayExtra("orderOfQuestions");
        mode = intent.getStringExtra("mode");

        setQuestion(questionNum, answers, orderOfQuestions);



        try {
            InputStream in = openFileInput("anteckningarXYZ.txt");
            if(in != null) {
                InputStreamReader tmp = new InputStreamReader(in);

                BufferedReader br = new BufferedReader(tmp);

                EditText textEditor = (EditText) findViewById(R.id.editText);
                StringBuilder stringBuilder = new StringBuilder();
                String fullString;
                String checker;

                while ((checker = br.readLine()) != null) {
                    stringBuilder.append(checker);
                }

                in.close();

                fullString = stringBuilder.toString();
                textEditor.setText(fullString);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setQuestion(int questionNum, String[][] answers, int[] orderOfQuestions){
        ImageView img = (ImageView) findViewById(R.id.imageViewNote);
        TextView tx1 = (TextView) findViewById(R.id.questionNote);

        //TODO: fix
        if(mode.equals("xyz")) {
            tx1.setText(questionNum + ". " + answers[orderOfQuestions[questionNum - 1]][0]);
            if (answers[orderOfQuestions[questionNum - 1]][6].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[questionNum - 1]][7], "drawable", getPackageName()));
            } else{
                img.setBackgroundResource(0);
            }
        } else if(mode.equals("kva")){
            tx1.setText(questionNum + ". " + answers[orderOfQuestions[questionNum - 1]][0]
                    + "\n\n" + answers[orderOfQuestions[questionNum - 1]][1]
                    + "\n\n" + answers[orderOfQuestions[questionNum - 1]][2]);
            if (answers[orderOfQuestions[questionNum - 1]][8].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[questionNum - 1]][9], "drawable", getPackageName()));
            } else{
                img.setBackgroundResource(0);
            }
        } else if(mode.equals("nog")){
            tx1.setText(questionNum + ". " + answers[orderOfQuestions[questionNum - 1]][0]
                    + "\n\n" + answers[orderOfQuestions[questionNum - 1]][1]
                    + "\n\n" + answers[orderOfQuestions[questionNum - 1]][2]);
            if (answers[orderOfQuestions[questionNum - 1]][9].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[questionNum - 1]][10], "drawable", getPackageName()));
            } else{
                img.setBackgroundResource(0);
            }
        }
    }

    public void goBack(View view) {

        String STORETEXT = "anteckningarXYZ.txt";
        EditText textEditor = (EditText) findViewById(R.id.editText);
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
            out.write(textEditor.getText().toString());
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
