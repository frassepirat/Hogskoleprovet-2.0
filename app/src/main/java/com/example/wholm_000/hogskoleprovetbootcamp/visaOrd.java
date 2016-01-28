package com.example.wholm_000.hogskoleprovetbootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Wholm_000 on 2016-01-28.
 */
public class visaOrd extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_words);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String FILENAME = "words_to_learn.txt";

        FileInputStream fstream = null;
        try {
            fstream = openFileInput(FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String second_line = "";

        ArrayList<String> priorityWords = new ArrayList<String>();
        ListView lv = (ListView) findViewById(R.id.listView);

        try {
            while ((second_line = br.readLine()) != null) {
                priorityWords.add(second_line);
                second_line = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                priorityWords );

        lv.setAdapter(arrayAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                Intent goingBack = new Intent();
                setResult(RESULT_OK, goingBack);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
