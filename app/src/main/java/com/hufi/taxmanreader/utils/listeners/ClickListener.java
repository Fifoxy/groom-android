package com.hufi.taxmanreader.utils.listeners;

import com.hufi.taxmanreader.model.Ticket;
import com.hufi.taxmanreader.realm.RealmEvent;

public interface ClickListener {
    void onSearchItemClick(Ticket ticket);
    void onEventItemClick(RealmEvent event);
}
