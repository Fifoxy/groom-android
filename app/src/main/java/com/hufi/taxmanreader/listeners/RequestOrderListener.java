package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Order;

public interface RequestOrderListener {
    void onOrderReceived(Order order);
}
