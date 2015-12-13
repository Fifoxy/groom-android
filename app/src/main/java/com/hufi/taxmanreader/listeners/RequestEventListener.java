package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Event;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public interface RequestEventListener {
    void onResponseReceived(Event event);
}
