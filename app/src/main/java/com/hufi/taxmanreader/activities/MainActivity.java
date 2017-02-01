package com.hufi.taxmanreader.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.fragments.ScannerFragment;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.realm.RealmEvent;
import com.hufi.taxmanreader.realm.RealmPlace;
import com.hufi.taxmanreader.realm.RealmProduct;
import com.hufi.taxmanreader.utils.GroomBottomNavigation;
import com.hufi.taxmanreader.utils.GroomUtils;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GroomBottomNavigation.GroomBottomNavigationCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.scanner_button).setOnClickListener(this);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

        fetchEvents();
        showSyncStats();
        notifyUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.user_settings:
                Intent accountIntent = new Intent(this, AccountActivity.class);
                startActivity(accountIntent);
                break;
            case R.id.events:
                Intent eventIntent = new Intent(this, EventsActivity.class);
                startActivity(eventIntent);
                break;
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
        }
    }

    private void notifyUser() {
        String msg;
        if (GroomUtils.userConnected()) {
            msg = getString(R.string.connected);
        } else {
            msg = getString(R.string.notconnected);
        }

        TSnackbar snackbar = TSnackbar.make(findViewById(R.id.main_content), msg, TSnackbar.LENGTH_SHORT);

        final View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.colorAccent));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.whiteText));
        snackbar.show();
    }

    private void showSyncStats() {
        final SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(getString(R.string.groom_sync), Context.MODE_PRIVATE);
        final String lastSync = sharedPreferences.getString(getString(R.string.last_sync), getString(R.string.never_sync));
        final String eventsNumber = String.valueOf(Realm.getInstance(this).where(RealmEvent.class).findAll().size());
        final String productsNumber = String.valueOf(Realm.getInstance(this).where(RealmProduct.class).findAll().size());

        ((TextView) findViewById(R.id.lastSync)).setText(lastSync);
        ((TextView) findViewById(R.id.eventsNumber)).setText(eventsNumber);
        ((TextView) findViewById(R.id.productsNumber)).setText(productsNumber);
    }

    private void fetchEvents() {
        GroomApplication.service.getAllEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, retrofit2.Response<List<Event>> response) {
                Realm realm = Realm.getInstance(GroomApplication.getContext());
                realm.beginTransaction();
                realm.where(RealmEvent.class).findAll().clear();
                realm.where(RealmProduct.class).findAll().clear();
                realm.where(RealmPlace.class).findAll().clear();

                for (Event event : response.body()) {
                    if (realm.where(RealmEvent.class).equalTo("slug", event.getSlug()).count() == 0) {
                        RealmPlace place = realm.where(RealmPlace.class).equalTo("id", event.getPlace_id()).findFirst();
                        if (place == null) {
                            place = realm.createObject(RealmPlace.class);
                            place.setId(event.getPlace().getId());
                            place.setName(event.getPlace().getName());
                            place.setAddress(event.getPlace().getAddress());
                        }

                        RealmEvent realmEvent = realm.createObject(RealmEvent.class);
                        realmEvent.setSlug(event.getSlug());
                        realmEvent.setName(event.getName());
                        realmEvent.setPlace(place);

                        for (Product product : event.getProducts()) {
                            if (realm.where(RealmProduct.class).equalTo("id", product.getId()).count() == 0) {
                                RealmProduct realmProduct = realm.createObject(RealmProduct.class);
                                realmProduct.setId(product.getId());
                                realmProduct.setName(product.getName());
                                realmProduct.setPrice(product.getPrice());
                                realmProduct.setEvent(realmEvent);
                            }
                        }
                    }
                }

                realm.commitTransaction();
                Toast.makeText(GroomApplication.getContext(), getString(R.string.sync_success), Toast.LENGTH_LONG).show();
                final SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(getString(R.string.groom_sync), Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy kk:mm:ss");
                editor.putString(getString(R.string.last_sync), format.format(Calendar.getInstance().getTime()));
                editor.apply();
                showSyncStats();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable throwable) {
                Toast.makeText(GroomApplication.getContext(), getString(R.string.sync_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onTabSelected(int position) {
        switch (position)
        {
            case GroomBottomNavigation.NAVIGATION_ITEM_HOME:
                break;
            case GroomBottomNavigation.NAVIGATION_ITEM_SCAN:
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.main_content, new ScannerFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case GroomBottomNavigation.NAVIGATION_ITEM_ACCOUNT:
                break;
            default:
                return false;
        }
        return true;
    }
}
