package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Wholm_000 on 2015-11-16.
 */
public class kvantitativDel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.kvantitativ_del);
        TextView he = (TextView) findViewById(R.id.numCorrect);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Button xyz = (Button) findViewById(R.id.xyzButton);
        Button kva = (Button) findViewById(R.id.kvaButton);
        Button nog = (Button) findViewById(R.id.nogButton);
        Button dtk = (Button) findViewById(R.id.dtkButton);

        //kva.setEnabled(false);
        nog.setEnabled(false);
        dtk.setEnabled(false);

        Intent data = getIntent();

        if(data.getIntExtra("numQuestions", 0) != 0) {
            TextView scoreDisplay = (TextView) findViewById(R.id.numCorrect);

            int correct = data.getIntExtra("correctAnswers", 0);
            int numQue = data.getIntExtra("numQuestions", 0);

            scoreDisplay.setText("Du hade " + correct + " rätt av " + numQue + " möjliga.");
        }

    }
    public void gotoQuizxyz(View view) {

        Intent getNameScreenIntent = new Intent(this, firstQuestionScreen.class);

        final int result = 1;

        getNameScreenIntent.putExtra("mode", "xyz");

        startActivityForResult(getNameScreenIntent, result);

    }
    public void gotoQuizkva(View view) {
        Intent getNameScreenIntent = new Intent(this, firstQuestionScreen.class);

        final int result = 1;

        getNameScreenIntent.putExtra("mode", "kva");

        startActivityForResult(getNameScreenIntent, result);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            TextView scoreDisplay = (TextView) findViewById(R.id.numCorrect);

            int correct = data.getIntExtra("correctAnswers", 0);
            int numQue = data.getIntExtra("numQuestions", 0);

            scoreDisplay.setText("Du hade " + correct + " rätt av " + numQue + " möjliga.");
        }

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
