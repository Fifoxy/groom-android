package com.hufi.taxmanreader.async;

import android.os.AsyncTask;

import com.hufi.taxmanreader.listeners.RequestEventListener;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.utils.Requests;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class RequestEventAsyncTask extends AsyncTask<String, Integer, Event> {

    private RequestEventListener requestEventListener;

    public RequestEventAsyncTask(RequestEventListener requestEventListener){
        this.requestEventListener = requestEventListener;
    }

    @Override
    protected Event doInBackground(String... params) {
        return Requests.searchEvent(params[0]);
    }

    @Override
    protected void onPostExecute(Event event) {
        this.requestEventListener.onEventReceived(event);
    }
}
