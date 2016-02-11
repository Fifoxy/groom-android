package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Event;

public interface RequestEventListener {
    void onEventReceived(Event event);
}
