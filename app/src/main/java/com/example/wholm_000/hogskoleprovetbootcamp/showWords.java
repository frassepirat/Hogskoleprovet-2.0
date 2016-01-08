package com.example.wholm_000.hogskoleprovetbootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Wholm_000 on 2015-12-28.
 */
public class showWords extends AppCompatActivity {

    ArrayList<String> ord = new ArrayList<String>();
    ArrayList<String> svar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_words_screen);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //read words and meaning from textfiles.
        ListView words = (ListView) findViewById(R.id.listView);
        getWords();

        ArrayList<String> full = new ArrayList<String>();

        for(int i = 0; i < ord.size(); i++){
            if(i < svar.size()) {
                full.add(ord.get(i) + " - " + svar.get(i));
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, full);

        words.setAdapter(arrayAdapter);

    }
    void getWords(){
        InputStream is = getResources().openRawResource(R.raw.ord);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        InputStream is2 = getResources().openRawResource(R.raw.forklaringar);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(is2));


        try{
            while((line = reader.readLine()) != null){
                ord.add(line);
                line = "";
            }
            line = "";
            while((line = reader2.readLine()) != null){
                svar.add(line);
                line = "";
            }
        }catch (IOException e){
            Log.e("lol", "I got an error", e);
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
