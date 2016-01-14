package com.hufi.taxmanreader.async;

import android.os.AsyncTask;

import com.hufi.taxmanreader.listeners.RequestUserListener;
import com.hufi.taxmanreader.model.User;
import com.hufi.taxmanreader.utils.Requests;


public class RequestUserAsyncTask extends AsyncTask<String, Integer, User> {

    private RequestUserListener requestUserListener;

    public RequestUserAsyncTask(RequestUserListener requestUserListener){
        this.requestUserListener = requestUserListener;
    }

    @Override
    protected User doInBackground(String... params) {
        return Requests.searchUser();
    }

    @Override
    protected void onPostExecute(User user) {
        this.requestUserListener.onUserReceived(user);
    }
}