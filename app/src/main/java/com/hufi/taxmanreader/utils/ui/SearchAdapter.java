package com.hufi.taxmanreader.utils.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.model.Ticket;
import com.hufi.taxmanreader.utils.listeners.ClickListener;

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
                holder.firstname.setText(ticket.getPerson().getFirst_name());
                holder.lastname.setText(ticket.getPerson().getLast_name());
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