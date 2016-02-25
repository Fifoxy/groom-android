package com.hufi.taxmanreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Ticket;
import com.hufi.taxmanreader.realm.RealmEvent;
import com.hufi.taxmanreader.utils.listeners.ClickListener;
import com.hufi.taxmanreader.utils.ui.EventsAdapter;

import java.util.List;

import io.realm.Realm;

public class EventsActivity extends AppCompatActivity implements ClickListener{

    private Toolbar toolbar;
    private RecyclerView view;
    private EventsAdapter eventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        this.toolbar = (Toolbar) findViewById(R.id.events_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.events_title));

        this.view = (RecyclerView) findViewById(R.id.events_view);

        view.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(GroomApplication.getContext());
        view.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
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

    private void initialize(){
        List<RealmEvent> events = Realm.getInstance(GroomApplication.getContext()).allObjects(RealmEvent.class);

        eventsAdapter = new EventsAdapter(events, this);
        view.setAdapter(eventsAdapter);
    }

    @Override
    public void onSearchItemClick(Ticket ticket) {

    }

    @Override
    public void onEventItemClick(RealmEvent event) {
        Intent intent = new Intent(GroomApplication.getContext(), EventActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(getString(R.string.events), convertEvent(event));
        intent.putExtras(extras);
        startActivity(intent);
    }

    private Event convertEvent(RealmEvent event){
        Event result = new Event();
        result.setName(event.getName());
        result.setSlug(event.getSlug());
        result.getPlace().setName(event.getPlace().getName());
        result.getPlace().setAddress(event.getPlace().getAddress());
        result.getPlace().setId(event.getPlace().getId());
        return result;
    }

}

