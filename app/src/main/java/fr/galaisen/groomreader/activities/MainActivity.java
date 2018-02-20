package fr.galaisen.groomreader.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import fr.galaisen.groomreader.GroomApplication;
import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.fragments.ResultFragment;
import fr.galaisen.groomreader.fragments.ScannerFragment;
import fr.galaisen.groomreader.fragments.SearchFragment;
import fr.galaisen.groomreader.model.Event;
import fr.galaisen.groomreader.model.Product;
import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.realm.RealmEvent;
import fr.galaisen.groomreader.realm.RealmPlace;
import fr.galaisen.groomreader.realm.RealmProduct;
import fr.galaisen.groomreader.utils.GroomBottomNavigation;
import fr.galaisen.groomreader.utils.GroomUtils;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GroomBottomNavigation.GroomBottomNavigationCallback {


    @OnClick(R.id.byid_button)
    public void byIdClick(View view)
    {
        launchDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

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
                       /* RealmPlace place = realm.where(RealmPlace.class).equalTo("id", event.getPlace_id()).findFirst();
                        if (place == null) {
                            place = realm.createObject(RealmPlace.class);
                            place.setId(event.getPlace().getId());
                            place.setName(event.getPlace().getName());
                            place.setAddress(event.getPlace().getAddress());
                        }

                        RealmEvent realmEvent = realm.createObject(RealmEvent.class);
                        realmEvent.setSlug(event.getSlug());
                        realmEvent.setName(event.getName());
                        realmEvent.setPlace(place);*/

                        for (Product product : event.getProducts()) {
                            if (realm.where(RealmProduct.class).equalTo("id", product.getId()).count() == 0) {
                                RealmProduct realmProduct = realm.createObject(RealmProduct.class);
                                realmProduct.setId(product.getId());
                                realmProduct.setName(product.getName());
                                realmProduct.setPrice(product.getPrice());
                                //realmProduct.setEvent(realmEvent);
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
               // showSyncStats();
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

    private void launchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.scanner, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Dialog f = (Dialog) dialog;
                EditText ticketID = (EditText) f.findViewById(R.id.dialog_ticket_id);

                if (GroomUtils.userConnected()) {
                    GroomApplication.service.getTicket(Integer.valueOf(ticketID.getText().toString())).enqueue(new Callback<Ticket>() {
                        @Override
                        public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                            if (response.code() == 200) {
                                ResultFragment fragment = ResultFragment.newInstance(null, response.body(), true);
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.main_content, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                if (response.code() == 404) {
                                    Toast.makeText(GroomApplication.getContext(), getString(R.string.not_found), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(GroomApplication.getContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Ticket> call, Throwable t) {
                            Toast.makeText(GroomApplication.getContext(), getString(R.string.service_failure), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
            }
        });

        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_scan, null));

        AlertDialog dialog = builder.create();
        dialog.show();

        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);


        negative.setTextColor(getResources().getColor(R.color.colorAccent));
        positive.setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
