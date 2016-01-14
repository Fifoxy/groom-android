package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.User;

/**
 * Created by pierre on 14/01/16.
 */
public interface RequestUserListener {
    void onUserReceived(User user);
}
