package com.hufi.taxmanreader.async;

import android.os.AsyncTask;

import com.hufi.taxmanreader.listeners.RequestProductListener;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.utils.Requests;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class RequestProductAsyncTask extends AsyncTask<String, Integer, Product> {

    private RequestProductListener requestProductListener;

    public RequestProductAsyncTask(RequestProductListener requestProductListener){
        this.requestProductListener = requestProductListener;
    }

    @Override
    protected Product doInBackground(String... params) {
        return Requests.searchProduct(Integer.valueOf(params[0]));
    }

    @Override
    protected void onPostExecute(Product product) {
        this.requestProductListener.onResponseReceived(product);
    }
}
