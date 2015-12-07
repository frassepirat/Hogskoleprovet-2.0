package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by Wholm_000 on 2015-12-07.
 */
public class antalFragor extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antal_fragor);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        NumberPicker noPick = (NumberPicker) findViewById(R.id.numberPicker);

        noPick.setMaxValue(50);
        noPick.setMinValue(1);
    }

    public void goBackFromNumPick(View view) {
        Intent intent = new Intent(this, firstQuestionScreen.class);
        Intent prevInten = getIntent();

        NumberPicker noPick = (NumberPicker) findViewById(R.id.numberPicker);
        int numQuestions = noPick.getValue();

        intent.putExtra("numOfQuestionsToRead", numQuestions);
        intent.putExtra("mode", prevInten.getStringExtra("mode"));

        startActivity(intent);
        finish();
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
