package com.hufi.taxmanreader.utils.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.realm.RealmEvent;
import com.hufi.taxmanreader.utils.listeners.ClickListener;


public class EventsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView view;
    public TextView name;
    private ClickListener listener;

    private RealmEvent event;

    public EventsHolder(View itemView) {
        super(itemView);

        view = (CardView) itemView.findViewById(R.id.cv_events);
        name = (TextView) itemView.findViewById(R.id.name_event);
    }

    public void setView(RealmEvent event, ClickListener listener) {
        this.listener = listener;
        this.event = event;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.listener.onEventItemClick(event);
    }
}
