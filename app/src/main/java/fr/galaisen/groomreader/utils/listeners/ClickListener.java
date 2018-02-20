package fr.galaisen.groomreader.utils.listeners;

import fr.galaisen.groomreader.model.Ticket;
import fr.galaisen.groomreader.realm.RealmEvent;

public interface ClickListener {
    void onSearchItemClick(Ticket ticket);
    void onEventItemClick(RealmEvent event);
}
