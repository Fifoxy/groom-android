package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.Product;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public interface RequestProductListener {
    void onResponseReceived(Product product);
}
