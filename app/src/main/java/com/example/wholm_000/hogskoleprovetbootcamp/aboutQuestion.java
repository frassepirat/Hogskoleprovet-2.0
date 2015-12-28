package com.example.wholm_000.hogskoleprovetbootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Wholm_000 on 2015-11-28.
 */
public class aboutQuestion extends AppCompatActivity {

    String mode = "xyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_question);

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
    }




    public void setQuestion(int questionNum, String[][] answers, int[] orderOfQuestions){
        ImageView img = (ImageView) findViewById(R.id.aboutImageView);
        TextView tx1 = (TextView) findViewById(R.id.aboutTextView);
        TextView tx2 = (TextView) findViewById(R.id.aboutTextViewHow);

        //TODO: fix
        if(mode.equals("xyz")) {
            tx1.setText(questionNum + ". " + answers[orderOfQuestions[questionNum - 1]][0]);
            if (answers[orderOfQuestions[questionNum - 1]][6].equals("yes")) {
                img.setBackgroundResource(getResources().getIdentifier(answers[orderOfQuestions[questionNum - 1]][7], "drawable", getPackageName()));
            } else{
                img.setBackgroundResource(0);
            }
            tx2.setText(answers[orderOfQuestions[questionNum - 1]][8]);
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
        tx2.setText(answers[orderOfQuestions[questionNum - 1]][11]);
    }
    }
    public void goBack2(View view) {

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
