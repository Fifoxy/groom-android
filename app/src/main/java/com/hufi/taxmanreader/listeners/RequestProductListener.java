package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Product;

public interface RequestProductListener {
    void onProductReceived(Product product);
}
