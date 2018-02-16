package fr.galaisen.groomreader.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.galaisen.groomreader.GroomApplication;
import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.realm.RealmEvent;
import fr.galaisen.groomreader.utils.listeners.ClickListener;
import fr.galaisen.groomreader.utils.ui.SearchAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements ClickListener {
    private View rootView;

    private RecyclerView results;
    private TextView no_results;
    private SearchAdapter searchAdapter;

    private String lastname;

    private List<Ticket> tickets;

    public static SearchFragment newInstance(String lastname) {
        final SearchFragment searchFragment = new SearchFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(GroomApplication.getContext().getString(R.string.search_lastname), lastname);
        searchFragment.setArguments(arguments);
        return searchFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.search_fragment, container, false);
        results = (RecyclerView) this.rootView.findViewById(R.id.results_view);
        no_results = (TextView) this.rootView.findViewById(R.id.no_result_found);

        results.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(GroomApplication.getContext());
        results.setLayoutManager(layoutManager);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.search_result));

        load();
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
        lastname = getArguments().getString(GroomApplication.getContext().getString(R.string.search_lastname));

        GroomApplication.service.findTicketByLastName(lastname).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                setRecyclerView(response.body());
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                no_results.setText(getString(R.string.service_failure));
                no_results.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setRecyclerView(List<Ticket> tickets) {
        if (!tickets.isEmpty()) {
            searchAdapter = new SearchAdapter(tickets, this);
            results.setAdapter(searchAdapter);
        } else {
            no_results.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSearchItemClick(Ticket ticket) {
        if (ticket != null) {
            ResultFragment fragment = ResultFragment.newInstance(null, ticket, true);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.scanner_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onEventItemClick(RealmEvent event) {

    }


}
