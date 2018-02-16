package fr.galaisen.groomreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fr.galaisen.groomreader.GroomApplication;
import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.Event;
import fr.galaisen.groomreader.model.Place;
import fr.galaisen.groomreader.model.Product;
import fr.galaisen.groomreader.utils.ui.ProductsAdapter;
import com.victor.loading.rotate.RotateLoading;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Event event;

    private TextView place;
    private TextView address;

    private RecyclerView list_products;
    private ProductsAdapter productsAdapter;

    private RotateLoading progress_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        this.toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        place = (TextView) findViewById(R.id.a_event_place);
        address = (TextView) findViewById(R.id.a_event_address);
        progress_products = (RotateLoading) findViewById(R.id.progress_products);
        list_products = (RecyclerView) findViewById(R.id.products_view);

        list_products.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(GroomApplication.getContext());
        list_products.setLayoutManager(layoutManager);

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
        progress_products.setVisibility(View.VISIBLE);
        progress_products.start();
        Place place_infos = event.getPlace();
        place.setText(place_infos.getName());
        address.setText(place_infos.getAddress());

        GroomApplication.service.getProductsByEvent(event.getSlug()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
                Collections.sort(products);
                productsAdapter = new ProductsAdapter(products);
                list_products.setAdapter(productsAdapter);
                progress_products.stop();
                progress_products.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(GroomApplication.getContext(), getString(R.string.service_failure), Toast.LENGTH_SHORT).show();
                progress_products.stop();
                progress_products.setVisibility(View.GONE);
            }
        });
    }
}
