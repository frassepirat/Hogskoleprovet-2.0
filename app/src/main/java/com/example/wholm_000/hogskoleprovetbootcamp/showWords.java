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

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    ArrayList<Integer> dagarForToday = new ArrayList<Integer>();

    int wordChoice;
    int meaningNum = 0;
    int daysUntilPractice = 0;

    private boolean debug = false;

    String FILENAME = "words_to_learn.txt";

    //1 = lätt, 2 = medel, 3 = svår, 0 = inget.
    int selected = 0;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_words_screen);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Button b4 = (Button) findViewById(R.id.debugButton);

        b4.setVisibility(View.INVISIBLE);

        if(debug){
            b4.setVisibility(View.VISIBLE);
        }

        //read words and meaning from textfiles.
        //ListView words = (ListView) findViewById(R.id.listView);
        try {
            getWords();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        int[] zeroWords = new int[dagar.size()];

        //get all words that need to be praciticed for today.
        zeroWords = sortWords(zeroWords);

        int numWords = 1;
        if(zeroWords.length > 10) {
            numWords = 10;
        } else{
            numWords = zeroWords.length;
        }



        if(numWords != 0){
            for(int i = 0; i < numWords; i++){
                wordsForToday.add(ord.get(zeroWords[i]));
                meaningForToday.add(svar.get(zeroWords[i]));
                dagarForToday.add(dagar.get(zeroWords[i]));
            }
        } else{
            wordsForToday.add("DU KAN ALLA ORD.");
            meaningForToday.add("BRA JOBBAT! KOM TILLBAKA IMORGON.");
            dagarForToday.add(600);
        }


        //set button numbers to days it will add.
        Button b1 = (Button) findViewById(R.id.good);
        Button b2 = (Button) findViewById(R.id.okay);
        Button b3 = (Button) findViewById(R.id.bad);

        int numDaysToWait = dagarForToday.get(0);

        int b1Increase = set_days(1, numDaysToWait);
        int b2Increase = set_days(2, numDaysToWait);
        int b3Increase = set_days(3, numDaysToWait);

        b1.setText("LÄTT(" + b1Increase + ")");
        b2.setText("MEDEL(" + b2Increase + ")");
        b3.setText("SVÅR(" + b3Increase + ")");



        //TODO: Write current date + diffcullty level to a new textfile in internal storage.
        //everytime you start ord. Read that file and see if the date is less or equal to the date of the diffculty date.
        //if there is less than 20 0's in that file, take new ones from the raw file.
        // http://developer.android.com/guide/topics/data/data-storage.html#filesInternal

        TextView tx1 = (TextView) findViewById(R.id.ordandfork);
        tx1.setText(wordsForToday.get(meaningNum));

        TextView numWordsLeft = (TextView) findViewById(R.id.numWordsLeft);
        numWordsLeft.setText(Integer.toString(wordsForToday.size() - meaningNum));
    }
    void getWords() throws FileNotFoundException {
        InputStream is = getResources().openRawResource(R.raw.ordmedfork);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        File file = getFileStreamPath(FILENAME);

       if(file.exists()) {
            FileInputStream fstream = openFileInput(FILENAME);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String second_line = "";

            ArrayList<String> priorityWords = new ArrayList<String>();

            try {
                while ((second_line = br.readLine()) != null) {
                    priorityWords.add(second_line);
                    second_line = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < priorityWords.size(); i++) {
                String[] buffer = priorityWords.get(i).split("%");
                //compare with todays date, if it's below or equal to zero read it otherwise do nothing.
                String[] todays_date = getDate().split("/");
                String[] prev_date = buffer[3].split("/");

                int pdd = Integer.parseInt(prev_date[0]);
                int pyy = Integer.parseInt(prev_date[2]);
                int pmm = Integer.parseInt(prev_date[1]);

                int tdd = Integer.parseInt(todays_date[0]);
                int tmm = Integer.parseInt(todays_date[1]);
                int tyy = Integer.parseInt(todays_date[2]);

                int daysSinceLearn = 0;

                if (tyy - pyy > 0) {
                    daysSinceLearn += (tyy - pyy) * 365;
                } else if (tmm - pmm > 0) {
                    daysSinceLearn += (tmm - pmm) * 31;
                } else if (tdd - pdd > 0) {
                    daysSinceLearn += tdd - pdd;
                }

                int numDaysToWait = Integer.parseInt(buffer[2]);

                if (daysSinceLearn >= numDaysToWait) {
                    ord.add(buffer[0]);
                    svar.add(buffer[1]);
                    //-1 gets first priority
                    dagar.add(Integer.parseInt(buffer[2]));
                }


            }
            //shuffle all with same seed to get same shuffle.
            long seed = System.nanoTime();
            Collections.shuffle(ord, new Random(seed));
            Collections.shuffle(svar, new Random(seed));
            Collections.shuffle(dagar, new Random(seed));


        }
        try{
            while((line = reader.readLine()) != null){
                both.add(line);
                line = "";
            }
            line = "";
        }catch (IOException e){
            Log.e("lol", "I got an error", e);
        }

        long seed2 = System.nanoTime();
        Collections.shuffle(both, new Random(seed2));

        for(int i = 0; i < both.size(); i++){
            String[] buffer = both.get(i).split("%");

            ord.add(buffer[0]);
            svar.add(buffer[1]);
            dagar.add(-1);
        }





    }
    int[] sortWords(int zeroWords[]){

        int pos = 0;

        for(int i = 0; i < dagar.size(); i++){
            if(dagar.get(i) != -1){
                zeroWords[pos] = i;
                pos++;
            }
        }
        for(int i = 0; i < dagar.size(); i++){
            if(dagar.get(i) == -1){
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

                Intent goingBack = new Intent();
                setResult(RESULT_OK, goingBack);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog2(View view) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Läs ordet och tänk vad du tror att det är. Tryck sedan på svar och se om du gissade rätt." +
                "Välj hur bra du kunde ordet och spela vidare. Träna varje dag för att lära dig orden.");
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

            String wordToSave = wordsForToday.get(meaningNum);

            //remove this word from textfile.
            if(selected != 3) {
                removeLine(wordToSave, FILENAME);

                OutputStream fos = null;

                try {
                    fos = new BufferedOutputStream(openFileOutput(FILENAME, Context.MODE_APPEND));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                daysUntilPractice = dagarForToday.get(meaningNum);

                daysUntilPractice = set_days(selected, daysUntilPractice);
                String date = getDate();

                //example word = hej%att hälsa%4%20/10/16
                wordToSave = wordToSave.concat("%" + meaningForToday.get(meaningNum) + "%" + daysUntilPractice + "%" + date + "\n");


                //write word to file or put it in queue
                try {
                    fos.write(wordToSave.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos.close();
            } else{
                dagarForToday.add(0);
                wordsForToday.add(wordsForToday.get(meaningNum));
                meaningForToday.add(meaningForToday.get(meaningNum));
            }



            tx1.setText("");

            //quit when all words have been learned.
            if(meaningNum < wordsForToday.size() - 1) {

                meaningNum++;

                TextView numWordsLeft = (TextView) findViewById(R.id.numWordsLeft);
                numWordsLeft.setText(Integer.toString(wordsForToday.size() - meaningNum));

                tx2.setText(wordsForToday.get(meaningNum));
                warningText.setText("");
                selected = 0;
                b1.setTextColor(getResources().getColor(R.color.green));
                b2.setTextColor(getResources().getColor(R.color.yellow));
                b3.setTextColor(getResources().getColor(R.color.colorAccent));
            } else{
                b4.setText("AVSLUTA");
                tx1.setText(meaningForToday.get(meaningNum));
            }
            daysUntilPractice = dagarForToday.get(meaningNum);
            int b1Increase = set_days(1, daysUntilPractice);
            int b2Increase = set_days(2, daysUntilPractice);
            int b3Increase = set_days(3, daysUntilPractice);

            b1.setText("LÄTT(" + b1Increase + ")");
            b2.setText("MEDEL(" + b2Increase + ")");
            b3.setText("SVÅR(" + b3Increase + ")");
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
                case 2:
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
                    b = 1;
                    break;
            }
        } else if(i == 2){
            b = 1;
        } else{
            b = 0;
        }
        daysUntilPractice = b;

        return daysUntilPractice;
    }

    public void removeLine(String lineToRemove, String FILENAME) throws IOException {
        // Thanks to singleshot on stackoverflow for writing this function.
        // http://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it

        File inputFile = getFileStreamPath(FILENAME);
        OutputStream fos = null;

        try {
            fos = new BufferedOutputStream(openFileOutput("temp_file.txt", Context.MODE_APPEND));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(fos != null && reader != null) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] newLine = currentLine.split("%");
                if (newLine[0].equals(lineToRemove)) continue;
                fos.write((currentLine + System.getProperty("line.separator")).getBytes());
            }
            fos.close();
            reader.close();
            File tempFile = getFileStreamPath("temp_file.txt");
            boolean successful = tempFile.renameTo(inputFile);
        }
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

        //
        date = date.concat(Integer.toString(dd) + "/" + Integer.toString(mm + 1) + "/" + Integer.toString(yy));

        return date;
    }

    public void goToAllWords(View view) {
        Intent getNameScreenIntent;
        final int result = 1;
        getNameScreenIntent = new Intent(this, visaOrd.class);
        startActivityForResult(getNameScreenIntent, result);
    }
}
