package com.hufi.taxmanreader.async;

import android.os.AsyncTask;

import com.hufi.taxmanreader.listeners.RequestOrderListener;
import com.hufi.taxmanreader.model.Order;
import com.hufi.taxmanreader.utils.Requests;

/**
 * Created by Pierre Defache on 14/12/2015.
 */
public class RequestOrderAsyncTask extends AsyncTask<Integer, Integer, Order> {

    private RequestOrderListener requestOrderListener;

    public RequestOrderAsyncTask(RequestOrderListener requestOrderListener){
        this.requestOrderListener = requestOrderListener;
    }

    @Override
    protected Order doInBackground(Integer... params) {
        return Requests.searchOrder(params[0]);
    }

    @Override
    protected void onPostExecute(Order order) {
        this.requestOrderListener.onOrderReceived(order);
    }
}
