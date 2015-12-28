package com.example.wholm_000.hogskoleprovetbootcamp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Wholm_000 on 2015-12-09.
 */
public class readingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reading_activity);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    public void goBackFromReading(View view) {
        Intent goingBack = new Intent();
        setResult(RESULT_OK, goingBack);
        finish();
    }

    public void goToDNSverige(View view) {
        goToUrl("http://www.dn.se/nyheter/sverige/");
    }
    public void goToDnPolitik(View view) {
        goToUrl("http://www.dn.se/nyheter/politik/");
    }
    public void goToDnVetenskap(View view) {
        goToUrl("http://www.dn.se/nyheter/vetenskap/");
    }
    public void goToDnVarlden(View view) {
        goToUrl("http://www.dn.se/nyheter/varlden/");
    }
    public void goToDnKlimatet(View view) { goToUrl("http://www.dn.se/nyheter/klimatmotet/"); }
    public void goToDnKultur(View view) {
        goToUrl("http://www.dn.se/kultur-noje/");
    }

    private void goToUrl(String url){
        Uri uriUri = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUri);
        startActivity(launchBrowser);
    }


}
