package com.hufi.taxmanreader.fragments;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.GroomApplication;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Order;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.model.Ticket;
import com.hufi.taxmanreader.utils.TaxmanUtils;
import com.victor.loading.rotate.RotateLoading;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private String result;

    private boolean manual;

    private FloatingActionButton status;
    private TextView statusText;
    private TextView lastname;
    private TextView firstname;
    private TextView ticket_id;
    private TextView revoked;

    private TextView product_name;
    private TextView product_price;

    private TextView event_name;
    private TextView event_location;

    private CardView product_information;
    private TextView product_label;
    private CardView event_information;
    private TextView event_label;

    private RotateLoading event_loading;
    private RotateLoading ticket_loading;

    private Button validate_button;

    public static ResultFragment newInstance(String result, Boolean isManual) {
        final ResultFragment resultFragment = new ResultFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(GroomApplication.getContext().getString(R.string.scanner_result), result);
        arguments.putBoolean(GroomApplication.getContext().getString(R.string.scanner_manual), isManual);
        resultFragment.setArguments(arguments);
        return resultFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.result_fragment, container, false);
        status = (FloatingActionButton) this.rootView.findViewById(R.id.fab);
        statusText = (TextView) this.rootView.findViewById(R.id.status);
        lastname = (TextView) this.rootView.findViewById(R.id.lastname);
        firstname = (TextView) this.rootView.findViewById(R.id.firstname);
        ticket_id = (TextView) this.rootView.findViewById(R.id.ticket_id);
        revoked = (TextView) this.rootView.findViewById(R.id.revoked);

        product_name = (TextView) this.rootView.findViewById(R.id.product_name);
        product_price = (TextView) this.rootView.findViewById(R.id.product_price);

        event_name = (TextView) this.rootView.findViewById(R.id.event_name);
        event_location = (TextView) this.rootView.findViewById(R.id.event_location);

        product_information = (CardView) this.rootView.findViewById(R.id.product_information);
        event_information = (CardView) this.rootView.findViewById(R.id.event_information);

        product_label = (TextView) this.rootView.findViewById(R.id.ticket_label);
        event_label = (TextView) this.rootView.findViewById(R.id.event_label);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.scanner_result));

        event_loading = (RotateLoading) this.rootView.findViewById(R.id.progress_event);
        ticket_loading = (RotateLoading) this.rootView.findViewById(R.id.progress_ticket);

        validate_button = (Button) this.rootView.findViewById(R.id.validate_button);
        validate_button.setOnClickListener(this);

        if(!manual) {
            load();
        } else {
            //@TODO
        }

        return this.rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void load() {
        result = getArguments().getString(getString(R.string.scanner_result));

        assert result != null;
        if (!result.isEmpty()) {
            success();
            event_loading.start();
            ticket_loading.start();
        } else {
            failure(getString(R.string.invalid));
        }
    }

    private void success() {
        status.setImageDrawable(getActivity().getDrawable(R.drawable.ic_done));
        status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(GroomApplication.getContext(), R.color.granted)));
        statusText.setText(getString(R.string.access_granted));
        statusText.setTextColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.granted));

        Gson gson = new Gson();
        Ticket ticket = gson.fromJson(result, Ticket.class);

        lastname.setText(ticket.getLastname());
        firstname.setText(ticket.getFirstname());
        ticket_id.setText("ID: " + ticket.getTicket_id());

        GroomApplication.service.getProduct(Integer.valueOf(ticket.getPrid())).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product = response.body();
                if (product != null) {
                    product_name.setText(product.getName());
                    product_price.setText("\t\t\t" + product.getPrice() + "€");

                    GroomApplication.service.getEvent(product.getEvent_slug()).enqueue(new Callback<Event>() {
                        @Override
                        public void onResponse(Call<Event> call, Response<Event> response) {
                            Event event = response.body();
                            if (event != null) {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(event.getName());

                                event_name.setText(event.getName());
                                event_location.setText(event.getPlace().getName());
                            }

                            event_loading.stop();
                        }

                        @Override
                        public void onFailure(Call<Event> call, Throwable throwable) {

                        }
                    });
                }

                ticket_loading.stop();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {

            }
        });

        if (TaxmanUtils.userConnected()) {
            GroomApplication.service.getOrder(Integer.valueOf(ticket.getOrid())).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    Order order = response.body();
                    if (order != null) {
                        if(order.getRevoked()){
                            failure(getString(R.string.revoked));
                        } else {
                            validate_button.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable throwable) {

                }
            });
        }
    }

    private void failure(String msg) {
        status.setImageDrawable(getActivity().getDrawable(R.drawable.ic_block));
        status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(GroomApplication.getContext(), R.color.denied)));
        statusText.setText(getString(R.string.access_denied));
        statusText.setTextColor(ContextCompat.getColor(GroomApplication.getContext(), R.color.denied));

        revoked.setText(msg);
        revoked.setVisibility(View.VISIBLE);

        hideCards();
    }

    private void hideCards() {
        product_information.setVisibility(View.GONE);
        event_information.setVisibility(View.GONE);
        product_label.setVisibility(View.GONE);
        event_label.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.validate_button){
            Toast.makeText(GroomApplication.getContext(), "@TODO", Toast.LENGTH_SHORT).show();
            getActivity().getFragmentManager().popBackStack();
        }
    }
}
