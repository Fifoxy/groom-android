package com.hufi.taxmanreader.listeners;

import com.hufi.taxmanreader.model.User;

public interface RequestUserListener {
    void onUserReceived(User user);
}
