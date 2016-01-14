package com.hufi.taxmanreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;
import com.hufi.taxmanreader.async.RequestUserAsyncTask;
import com.hufi.taxmanreader.listeners.RequestUserListener;
import com.hufi.taxmanreader.model.User;
import com.hufi.taxmanreader.utils.TaxmanUtils;


/**
 * Created by pierre on 14/01/16.
 */
public class AccountActivity extends AppCompatActivity implements View.OnClickListener, RequestUserListener {

    private Button signButton;
    private Toolbar toolbar;

    private TextView status;
    private TextView infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        this.toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.infos));

        this.signButton = (Button) findViewById(R.id.sign_in_button);
        this.signButton.setOnClickListener(this);

        this.status = (TextView) findViewById(R.id.sign_status);
        this.infos = (TextView) findViewById(R.id.sign_infos);
    }

    private void initialize(){
        this.infos.setVisibility(View.GONE);
        this.status.setVisibility(View.GONE);
        this.signButton.setVisibility(View.GONE);

        if(!TaxmanUtils.userConnected()){
            this.status.setText(getString(R.string.notconnected));
            this.signButton.setText(getString(R.string.sign_in));
            this.signButton.setVisibility(View.VISIBLE);
            this.status.setVisibility(View.VISIBLE);
        } else {
            RequestUserAsyncTask requestUserAsyncTask = new RequestUserAsyncTask(this);
            requestUserAsyncTask.execute();
        }
    }

    @Override
    protected void onResume() {
        initialize();
        super.onResume();
    }

    @Override
    protected void onStart() {
        initialize();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    finish();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            if(TaxmanUtils.userConnected()){
                SharedPreferences prefs = TaxmanReaderApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);
                prefs.edit().remove(getString(R.string.yoshimi_token)).apply();
                prefs.edit().remove(getString(R.string.access_token)).apply();
                Toast.makeText(TaxmanReaderApplication.getContext(), getString(R.string.valid_log_out), Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Intent intent = new Intent(this, YoshimiActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onUserReceived(User user) {
        if(user != null){
            String status = getString(R.string.isconnected) + " " + user.getPreferred_username();
            String infos = user.getFamily_name() + " " + user.getGiven_name();
            this.status.setText(status);
            this.infos.setText(infos);
            this.signButton.setText(getString(R.string.log_out));

            this.status.setVisibility(View.VISIBLE);
            this.infos.setVisibility(View.VISIBLE);
            this.signButton.setVisibility(View.VISIBLE);
        }
    }
}