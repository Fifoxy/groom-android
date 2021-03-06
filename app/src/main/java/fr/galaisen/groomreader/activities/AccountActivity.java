package fr.galaisen.groomreader.activities;

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

import fr.galaisen.groomreader.GroomApplication;
import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.User;
import fr.galaisen.groomreader.utils.GroomUtils;
import com.victor.loading.rotate.RotateLoading;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signButton;
    private Toolbar toolbar;

    private TextView status;
    private TextView infos;

    private RotateLoading progress_account_information;

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
        this.progress_account_information = (RotateLoading) findViewById(R.id.progress_account_information);
    }

    private void initialize() {
        this.infos.setVisibility(View.GONE);
        this.status.setVisibility(View.GONE);
        this.signButton.setVisibility(View.GONE);
        this.progress_account_information.setVisibility(View.VISIBLE);

        this.progress_account_information.start();

        if (!GroomUtils.userConnected()) {
            this.status.setText(getString(R.string.notconnected));
            this.signButton.setText(getString(R.string.sign_in));
            this.signButton.setVisibility(View.VISIBLE);
            this.status.setVisibility(View.VISIBLE);
            this.progress_account_information.stop();
            this.progress_account_information.setVisibility(View.GONE);
        } else {
            SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(GroomApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(GroomApplication.getContext().getString(R.string.yoshimi_token), "");

            GroomApplication.service.getUser("JWT " + token).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();

                    if (user != null) {
                        status.setText(getString(R.string.isconnected) + " " + user.getFirst_name() + " " + user.getLast_name());
                        infos.setText(user.getEmail());
                        signButton.setText(getString(R.string.log_out));

                        status.setVisibility(View.VISIBLE);
                        infos.setVisibility(View.VISIBLE);
                        signButton.setVisibility(View.VISIBLE);
                    }

                    progress_account_information.stop();
                    progress_account_information.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
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
            if (GroomUtils.userConnected()) {
                SharedPreferences prefs = GroomApplication.getContext().getSharedPreferences(getString(R.string.yoshimi), Context.MODE_PRIVATE);
                prefs.edit().remove(getString(R.string.yoshimi_token)).apply();
                prefs.edit().remove(getString(R.string.access_token)).apply();
                Toast.makeText(GroomApplication.getContext(), getString(R.string.valid_log_out), Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Intent intent = new Intent(this, YoshimiActivity.class);
                startActivity(intent);
            }
        }
    }
}