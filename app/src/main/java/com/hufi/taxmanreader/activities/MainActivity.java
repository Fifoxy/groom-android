package com.hufi.taxmanreader.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.fragments.ResultFragment;
import com.hufi.taxmanreader.utils.TaxmanUtils;

import java.security.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ticket_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.scanner_button).setOnClickListener(this);
        findViewById(R.id.check_button).setOnClickListener(this);
        ticket_ID = (EditText) findViewById(R.id.ID_text);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

        notifyUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.user_settings:
                Intent intent = new Intent(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanner_button) {
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.check_button){
           // REQUEST @TODO
            Toast.makeText(MainActivity.this, "ID = " + this.ticket_ID.getText().toString(), Toast.LENGTH_SHORT).show();
        }


    }

    private void notifyUser(){
        String msg = "";
        if(TaxmanUtils.userConnected()){
            msg = getString(R.string.connected);
        } else {
            msg = getString(R.string.notconnected);
        }

        TSnackbar snackbar = TSnackbar.make(findViewById(R.id.main_content), msg, TSnackbar.LENGTH_LONG);

        snackbar.addIcon(R.drawable.ic_action_user, 200);
                View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.colorAccent));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.whiteText));
        snackbar.show();
    }


    //@TODO
    private void launchResult(String jsonResult){
        ResultFragment fragment = ResultFragment.newInstance(jsonResult, true);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
