package fr.galaisen.groomreader.utils.ui;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.realm.RealmEvent;
import fr.galaisen.groomreader.utils.listeners.ClickListener;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsHolder> {

    private ClickListener clickListener;
    private List<RealmEvent> events;

    public EventsAdapter(List<RealmEvent> events, ClickListener listener) {
        this.events = events;
        this.clickListener = listener;
    }

    @Override
    public EventsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_items, parent, false);
        return new EventsHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsHolder holder, int position) {
        if (position < getItemCount()) {
            RealmEvent event = events.get(position);

            if (event != null) {
                holder.name.setText(event.getName());
                holder.setView(event, clickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
