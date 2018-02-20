package fr.galaisen.groomreader.fragments;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
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
import android.widget.TextView;

import com.google.gson.Gson;
import fr.galaisen.groomreader.GroomApplication;
import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.QRTicket;
import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.realm.RealmProduct;
import fr.galaisen.groomreader.utils.GroomUtils;
import com.victor.loading.rotate.RotateLoading;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFragment extends Fragment {
    private View rootView;
    private String result;

    private Ticket manual_ticket;

    private boolean manual;

    private FloatingActionButton status;
    private TextView statusText;
    private TextView lastname;
    private TextView firstname;
    private TextView ticket_id;
    private TextView status_message;

    private TextView product_name;
    private TextView product_price;

    private TextView event_name;
    private TextView event_location;

    private CardView product_information;
    private TextView product_label;
    private CardView event_information;
    private TextView event_label;

    private RotateLoading progress_check;

    private MediaPlayer mPlayer;

    public static ResultFragment newInstance(String result, Ticket ticket, Boolean isManual) {
        final ResultFragment resultFragment = new ResultFragment();
        final Bundle arguments = new Bundle();
        if (!isManual) {
            arguments.putString(GroomApplication.getContext().getString(R.string.scanner_result), result);
        } else {
            arguments.putParcelable(GroomApplication.getContext().getString(R.string.manual_result), ticket);
        }
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
        status_message = (TextView) this.rootView.findViewById(R.id.status_message);

        product_name = (TextView) this.rootView.findViewById(R.id.product_name);
        product_price = (TextView) this.rootView.findViewById(R.id.product_price);

        event_name = (TextView) this.rootView.findViewById(R.id.event_name);
        event_location = (TextView) this.rootView.findViewById(R.id.event_location);

        product_information = (CardView) this.rootView.findViewById(R.id.product_information);
        event_information = (CardView) this.rootView.findViewById(R.id.event_information);
        event_information.setVisibility(View.GONE); // Quickfix

        product_label = (TextView) this.rootView.findViewById(R.id.ticket_label);
        event_label = (TextView) this.rootView.findViewById(R.id.event_label);
        event_label.setVisibility(View.GONE); // Quickfix

        progress_check = (RotateLoading) this.rootView.findViewById(R.id.progress_check);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.scanner_result));

        manual = getArguments().getBoolean(GroomApplication.getContext().getString(R.string.scanner_manual));

        if (!manual) {
            load();
        } else {
            loadManual();
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
        progress_check.start();

        assert result != null;
        if (!result.isEmpty()) {
            success();
        } else {
            failure(getString(R.string.invalid));
        }
    }

    private void loadManual() {
        manual_ticket = getArguments().getParcelable(GroomApplication.getContext().getString(R.string.manual_result));
        progress_check.start();

        lastname.setText(manual_ticket.getLast_name());
        firstname.setText(manual_ticket.getFirst_name());
        ticket_id.setText(String.format("ID: %s", manual_ticket.getId()));

        setProduct(manual_ticket.getProduct().getId());
        checkUsage(manual_ticket.getId());
    }

    private void success() {
        Gson gson = new Gson();
        final QRTicket ticket = gson.fromJson(result, QRTicket.class);

        lastname.setText(ticket.getLastname());
        firstname.setText(ticket.getFirstname());
        ticket_id.setText(String.format("ID: %s", ticket.getTicket_id()));

        setProduct(Integer.valueOf(ticket.getPrid()));

        rootView.findViewById(R.id.status_message).setVisibility(View.GONE);

        if (GroomUtils.userConnected()) {
            checkUsage(Integer.valueOf(ticket.getTicket_id()));
        } else {
            stopProgress();
            beep();

            rootView.findViewById(R.id.status_message).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(R.id.status_message)).setText(getString(R.string.verif_failed));
            setFab(R.drawable.ic_block, R.color.colorAccent, R.string.access_unchecked);
        }
    }

    private void checkUsage(int ticketID) {
        GroomApplication.service.ticketUsage(ticketID).enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                stopProgress();

                if (response.code() != 200) {
                    beep();
                    rootView.findViewById(R.id.status_message).setVisibility(View.VISIBLE);
                    ((TextView) rootView.findViewById(R.id.status_message)).setText(getString(R.string.server_error_code) + response.code() + "\\n" + getString(R.string.couldnt_check));
                    setFab(R.drawable.ic_block, R.color.colorAccent, R.string.access_unchecked);
                } else {
                    Ticket ticket = response.body();

                    if (ticket.getError() == null) {
                        setFab(R.drawable.ic_done, R.color.granted, R.string.access_granted);
                        /*mPlayer = MediaPlayer.create(GroomApplication.getContext(), R.raw.bond_theme);
                        mPlayer.setLooping(false);
                        mPlayer.start();*/
                    } else if (ticket.getError().equals(getString(R.string.order_revoked))) {
                        failure(getString(R.string.revoked));
                        beep();
                    } else if (ticket.getError().equals(getString(R.string.ticket_used))) {
                        failure(getString(R.string.already_used));
                        beep();
                    }
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                stopProgress();
                beep();
                rootView.findViewById(R.id.status_message).setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.status_message)).setText(getString(R.string.reach_server));
                setFab(R.drawable.ic_block, R.color.colorAccent, R.string.access_unchecked);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null && mPlayer.isPlaying())
        {
            mPlayer.stop();
        }
    }

    private void setProduct(int prid) {
        RealmProduct product = Realm.getInstance(GroomApplication.getContext()).where(RealmProduct.class)
                .equalTo("id", prid)
                .findFirst();

        if (product != null) {
            product_name.setText(product.getName());
            product_price.setText(String.format("%s€", product.getPrice()));

            if (product.getEvent() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(product.getEvent().getName());
                event_name.setText(product.getEvent().getName());
                event_location.setText(product.getEvent().getPlace().getName());
            }
        }
    }

    private void failure(String msg) {
        stopProgress();
        setFab(R.drawable.ic_block, R.color.denied, R.string.access_denied);

        status_message.setText(msg);
        status_message.setVisibility(View.VISIBLE);

        hideCards();
    }

    private void setFab(int drawable, int color, int resId) {
        status.setImageDrawable(GroomApplication.getContext().getResources().getDrawable(drawable));
        status.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(GroomApplication.getContext(), color)));
        statusText.setText(getString(resId));
        statusText.setTextColor(ContextCompat.getColor(GroomApplication.getContext(), color));
    }


    private void hideCards() {
        product_information.setVisibility(View.GONE);
        event_information.setVisibility(View.GONE);
        product_label.setVisibility(View.GONE);
        event_label.setVisibility(View.GONE);
    }

    private void stopProgress() {
        if (progress_check.isStart()) progress_check.stop();
        progress_check.setVisibility(View.GONE);
    }

    private void beep() {
        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }
}
