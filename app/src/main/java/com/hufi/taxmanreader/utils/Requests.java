package com.hufi.taxmanreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Order;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.model.Ticket;
import com.hufi.taxmanreader.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Pierre Defache on 13/12/2015.
 */
public class Requests {

    public static final String HOST = "https://tickets.gala-isen.fr/api/";
    public static final String EVENT_PATH = "events/";
    public static final String PRODUCT_PATH = "products/";
    public static final String ORDER_PATH = "orders/";

    public static final String INFOS_HOST = "https://www.wapitisen.fr/sso/oauth/userinfo";

    public static Event searchEvent(String event_slug){
        try{
            String request = null;

            request = HOST + EVENT_PATH + event_slug;

            final HttpURLConnection connection;
            connection = (HttpURLConnection) new URL(request).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            final int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                String result = convertStreamToString(connection.getInputStream());

                Gson gson = new Gson();
                return gson.fromJson(result, Event.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Product searchProduct(Integer product_id){
        try{
            String request = null;

            request = HOST + PRODUCT_PATH + product_id.toString();

            final HttpURLConnection connection;
            connection = (HttpURLConnection) new URL(request).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            final int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                String result = convertStreamToString(connection.getInputStream());

                Gson gson = new Gson();
                return gson.fromJson(result, Product.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Order searchOrder(Integer order_id){
        try{
            String request = null;

            request = HOST + ORDER_PATH + order_id.toString();

            SharedPreferences sharedPreferences = TaxmanReaderApplication.getContext().getSharedPreferences(TaxmanReaderApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(TaxmanReaderApplication.getContext().getString(R.string.yoshimi_token), "");

            final HttpURLConnection connection;
            connection = (HttpURLConnection) new URL(request).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "JWT " + token);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            final int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                String result = convertStreamToString(connection.getInputStream());

                Gson gson = new Gson();
                return gson.fromJson(result, Order.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User searchUser(){
        try{
            String request = null;

            request = INFOS_HOST;

            SharedPreferences sharedPreferences = TaxmanReaderApplication.getContext().getSharedPreferences(TaxmanReaderApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(TaxmanReaderApplication.getContext().getString(R.string.access_token), "");

            final HttpURLConnection connection;
            connection = (HttpURLConnection) new URL(request).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            final int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                String result = convertStreamToString(connection.getInputStream());

                Gson gson = new Gson();
                return gson.fromJson(result, User.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
