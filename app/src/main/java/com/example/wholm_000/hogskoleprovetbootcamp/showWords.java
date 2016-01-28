package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Wholm_000 on 2015-12-28.
 */
public class showWords extends AppCompatActivity {

    ArrayList<String> both = new ArrayList<String>();
    ArrayList<String> ord = new ArrayList<String>();
    ArrayList<Integer> dagar = new ArrayList<Integer>();
    ArrayList<String> svar = new ArrayList<String>();

    ArrayList<String> wordsForToday = new ArrayList<String>();
    ArrayList<String> meaningForToday = new ArrayList<String>();

    int wordChoice;
    int meaningNum = 0;
    int daysUntilPractice = 0;

    //1 = lätt, 2 = medel, 3 = svår, 0 = inget.
    int selected = 0;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_words_screen);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        TextView tx1 = (TextView) findViewById(R.id.ordandfork);


        //read words and meaning from textfiles.
        //ListView words = (ListView) findViewById(R.id.listView);
        getWords();

        int[] zeroWords = new int[dagar.size()];

        //get all words that need to be praciticed for today.
        zeroWords = sortWords(zeroWords);

        int numWords = 1;
        if(zeroWords.length > 20) {
            numWords = 20;
        } else{
            numWords = zeroWords.length;
        }

        int random_range = zeroWords.length;
        Random randomizer = new Random();

        if(numWords != 0){
            for(int i = 0; i < numWords; i++){
                int choice = randomizer.nextInt(random_range);
                wordsForToday.add(ord.get(zeroWords[choice]));
                meaningForToday.add(svar.get(zeroWords[choice]));
            }
        } else{
            wordsForToday.add("DU KAN ALLA ORD.");
            meaningForToday.add("BRA JOBBAT! KOM TILLBAKA IMORGON.");
        }


        //TODO: Write current date + diffcullty level to a new textfile in internal storage.
        //everytime you start ord. Read that file and see if the date is less or equal to the date of the diffculty date.
        //if there is less than 20 0's in that file, take new ones from the raw file.
        // http://developer.android.com/guide/topics/data/data-storage.html#filesInternal


        tx1.setText(wordsForToday.get(meaningNum));


        /*wordChoice = ord.size();

        Random rand = new Random();
        wordChoice = rand.nextInt(wordChoice);

        tx1.setText(ord.get(wordChoice));

        ArrayList<String> full = new ArrayList<String>();

        /*for(int i = 0; i < ord.size(); i++){
            if(i < svar.size()) {
                full.add(ord.get(i) + " - " + svar.get(i));

            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, full);

        words.setAdapter(arrayAdapter);
*/
    }
    void getWords(){
        InputStream is = getResources().openRawResource(R.raw.ordmedfork);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;


        try{
            while((line = reader.readLine()) != null){
                both.add(line);
                line = "";
            }
            line = "";
        }catch (IOException e){
            Log.e("lol", "I got an error", e);
        }

        for(int i = 0; i < both.size(); i++){
            String[] buffer = both.get(i).split("%");

            ord.add(buffer[0]);
            svar.add(buffer[1]);
            dagar.add(Integer.parseInt((buffer[2])));
        }


    }
    int[] sortWords(int zeroWords[]){

        int pos = 0;

        for(int i = 0; i < dagar.size(); i++){
            if(dagar.get(i) == 0){
                zeroWords[pos] = i;
                pos++;
            }
        }
        return zeroWords;
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

    public void openDialog2(View view) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Läs ordet och tänk vad du tror att det är. Tryck sedan på svar och se om du gissade rätt." +
                "Välj sedan hur bra du kunde ordet och spela vidare. Träna varje dag för att lära dig orden.");
        dlgAlert.setTitle("Om Ord");
        dlgAlert.setNegativeButton("Ok", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void show_word_def(View view) throws IOException {

        Button b4 = (Button) findViewById(R.id.svarBut);
        TextView tx1 = (TextView) findViewById(R.id.svar);
        TextView tx2 = (TextView) findViewById(R.id.ordandfork);

        TextView warningText = (TextView) findViewById(R.id.warningTextWords);

        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        if(b4.getText().equals("AVSLUTA")){
            this.finish();
            Intent intent = new Intent(this, startScreen.class);

            startActivity(intent);
        } else if(b4.getText().equals("SVAR")) {
            b4.setText("NÄSTA");

            tx1.setText(meaningForToday.get(meaningNum));
        } else if(selected != 0){
            b4.setText("SVAR");

            tx1.setText("");

            String FILENAME = "words_to_learn.txt";
            String wordToSave = wordsForToday.get(meaningNum) + "\n";
            FileOutputStream fos = null;

            try {
                fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }




            //write word to file or put it in queue
            if(selected == 1){
                daysUntilPractice = set_days(1, daysUntilPractice);
                String date = getDate();
                wordToSave.concat("%" + daysUntilPractice + "%");
                try {
                    fos.write(wordToSave.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(selected == 2){

            }
            fos.close();


            //quit when all words have been learned.
            if(meaningNum < wordsForToday.size() - 1) {
                meaningNum++;
                tx2.setText(wordsForToday.get(meaningNum));
                warningText.setText("");
                selected = 0;
                b1.setTextColor(getResources().getColor(R.color.green));
                b2.setTextColor(getResources().getColor(R.color.yellow));
                b3.setTextColor(getResources().getColor(R.color.colorAccent));
            } else{
                b4.setText("AVSLUTA");
            }
        } else{
            warningText.setText("Välj hur bra du kunde ordet.");
        }

    }

    private int set_days(int i, int daysUntilPractice) {

        //b = buffer;
        int b = daysUntilPractice;

        if(i == 1) {

            switch (b) {
                case 1:
                    b = 3;
                    break;
                case 3:
                    b = 7;
                    break;
                case 7:
                    b = 15;
                    break;
                case 15:
                    b = 31;
                    break;
                case 31:
                    b = 4;
                    break;
                case 4:
                    b = 60;
                    break;
                case 60:
                    b = 120;
                    break;
                case 120:
                    b = 360;
                    break;
                case 360:
                    b = 600;
                    break;
                case 600:
                    b = 600;
                    break;
                default:
                    b = 0;
                    break;
            }
        } else{
            b = 1;
        }
        daysUntilPractice = b;

        return daysUntilPractice;
    }

    private void goToUrl(String url){
        Uri uriUri = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUri);
        startActivity(launchBrowser);
    }

    public void green(View view) {
        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        b1.setTextColor(getResources().getColor(R.color.green));

        b2.setTextColor(Color.BLACK);
        b3.setTextColor(Color.BLACK);
        selected = 1;
    }

    public void yellow(View view) {
        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        b2.setTextColor(getResources().getColor(R.color.yellow));

        b1.setTextColor(Color.BLACK);
        b3.setTextColor(Color.BLACK);
        selected = 2;
    }

    public void red(View view) {
        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        b3.setTextColor(getResources().getColor(R.color.colorAccent));

        b1.setTextColor(Color.BLACK);
        b2.setTextColor(Color.BLACK);
        selected = 3;
    }

    public void goToWordDef(View view) {
        TextView tx2 = (TextView) findViewById(R.id.ordandfork);
        String url = "http://www.synonymer.se/?query=" + tx2.getText();
        goToUrl(url);
    }

    public String getDate() {
        String date = "";

        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        date.concat(Integer.toString(dd) + "/" + Integer.toString(mm) + "/" + Integer.toString(yy));

        return date;
    }
}
