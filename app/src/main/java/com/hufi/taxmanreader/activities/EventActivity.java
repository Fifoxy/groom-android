package com.hufi.taxmanreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Place;

public class EventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Event event;

    private TextView place;
    private TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        this.toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = (TextView) findViewById(R.id.a_event_place);
        address = (TextView) findViewById(R.id.a_event_address);

        event = (Event) getIntent().getExtras().getParcelable(getString(R.string.events));
        if (event != null) {
            getSupportActionBar().setTitle(event.getName());
            initialize();
        } else {
            finish();
        }
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

    private void initialize() {
        Place place_infos = event.getPlace();
        place.setText(place_infos.getName());
        address.setText(place_infos.getAddress());
    }
}
