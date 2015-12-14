package com.hufi.taxmanreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;
import com.hufi.taxmanreader.utils.TaxmanUtils;

import java.security.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.scanner_button).setOnClickListener(this);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(TaxmanUtils.userConnected()){
            menu.findItem(R.id.action_sign).setTitle(getString(R.string.log_out));
        } else {
            menu.findItem(R.id.action_sign).setTitle(getString(R.string.sign_in));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_sign){
            if(TaxmanUtils.userConnected()){
                SharedPreferences prefs = TaxmanReaderApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);
                prefs.edit().remove(getString(R.string.yoshimi_token)).apply();
                item.setTitle(getString(R.string.sign_in));
            } else {
                Intent intent = new Intent(this, YoshimiActivity.class);
                startActivity(intent);
                item.setTitle(getString(R.string.log_out));
            }
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanner_button) {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        }
    }
}
