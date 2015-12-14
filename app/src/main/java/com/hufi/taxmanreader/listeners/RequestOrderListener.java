package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Order;

/**
 * Created by Pierre Defache on 14/12/2015.
 */
public interface RequestOrderListener {
    void onOrderReceived(Order order);
}
