package com.example.wholm_000.hogskoleprovetbootcamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Wholm_000 on 2015-11-19.
 */
public class startScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        Button verbalt = (Button) findViewById(R.id.button5);
        verbalt.setEnabled(false);

    }

    public void goToKvantitativ(View view) {
        Intent goToKvant = new Intent(this, kvantitativDel.class);
        startActivity(goToKvant);
    }

    public void openDialog(View view) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Ditt stöd till ett 2.0 resultat på högskoleprovet. " +
                "Tryck på den delen du vill träna på och välj svaren på frågorna.");
        dlgAlert.setTitle("Om Appen");
        dlgAlert.setNegativeButton("Ok", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
