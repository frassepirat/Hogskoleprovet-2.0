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

    String vilkenDel = "kvantitativ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.kvantitativ_del);
        TextView he = (TextView) findViewById(R.id.numCorrect);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        if(data.getStringExtra("vilkenDel") != null) {
            vilkenDel = data.getStringExtra("vilkenDel");
        }

        if(vilkenDel.equals("kvantitativ")) {

            Button xyz = (Button) findViewById(R.id.xyzButton);
            Button kva = (Button) findViewById(R.id.kvaButton);
            Button nog = (Button) findViewById(R.id.nogButton);
            Button dtk = (Button) findViewById(R.id.dtkButton);

            //kva.setEnabled(false);
            //nog.setEnabled(false);
            dtk.setEnabled(false);
        } else if(vilkenDel.equals("verbal")){
            Button ord = (Button) findViewById(R.id.xyzButton);
            Button läs = (Button) findViewById(R.id.kvaButton);
            Button elf = (Button) findViewById(R.id.nogButton);
            Button mek = (Button) findViewById(R.id.dtkButton);
            TextView subTitle = (TextView) findViewById(R.id.subTitleKvantDel);

            ord.setText("ord");
            läs.setText("läs");
            elf.setText("elf");
            mek.setText("mek");

            subTitle.setText(R.string.subTitleVerbala);

            //ord.setEnabled(false);
            mek.setEnabled(false);
            elf.setEnabled(false);

        }
        if(data.getIntExtra("numQuestions", 0) != 0) {
            TextView scoreDisplay = (TextView) findViewById(R.id.numCorrect);

            int correct = data.getIntExtra("correctAnswers", 0);
            int numQue = data.getIntExtra("numQuestions", 0);

            scoreDisplay.setText("Du hade " + correct + " rätt av " + numQue + " möjliga.");
        }

    }
    //ord
    public void gotoQuizxyz(View view) {

        Intent getNameScreenIntent;

        final int result = 1;

        if(vilkenDel.equals("kvantitativ")) {
            getNameScreenIntent = new Intent(this, antalFragor.class);
            getNameScreenIntent.putExtra("mode", "xyz");
        } else{
            getNameScreenIntent = new Intent(this, showWords.class);
        }
        startActivityForResult(getNameScreenIntent, result);

    }
    //läs
    public void gotoQuizkva(View view) {
        Intent getNameScreenIntent;

        final int result = 1;

        if(vilkenDel.equals("kvantitativ")) {
            getNameScreenIntent = new Intent(this, antalFragor.class);

            getNameScreenIntent.putExtra("mode", "kva");
        } else{
            getNameScreenIntent = new Intent(this, readingActivity.class);
        }
        startActivityForResult(getNameScreenIntent, result);

    }
    public void gotoQuiznog(View view) {
        Intent getNameScreenIntent = new Intent(this, antalFragor.class);

        final int result = 1;

        getNameScreenIntent.putExtra("mode", "nog");

        startActivityForResult(getNameScreenIntent, result);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            int test = data.getIntExtra("numQuestions", 0);

            if (test > 0) {
                TextView scoreDisplay = (TextView) findViewById(R.id.numCorrect);

                int correct = data.getIntExtra("correctAnswers", 0);
                int numQue = data.getIntExtra("numQuestions", 0);

                scoreDisplay.setText("Du hade " + correct + " rätt av " + numQue + " möjliga.");
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                Intent intent = new Intent(this, startScreen.class);

                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
