package fr.galaisen.groomreader.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.utils.listeners.ClickListener;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private List<Ticket> results;
    private ClickListener clickListener;

    public SearchAdapter(List<Ticket> tickets, ClickListener listener){
        this.results = tickets;
        this.clickListener = listener;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_items, parent, false);
        return new SearchHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        if(position < getItemCount()){
            Ticket ticket = results.get(position);

            if(ticket != null){
                holder.firstname.setText(ticket.getFirst_name());
                holder.lastname.setText(ticket.getLast_name());
                holder.id.setText(Integer.toString(ticket.getId()));

                holder.setView(ticket, clickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}