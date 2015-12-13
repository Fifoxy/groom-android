package com.hufi.taxmanreader.utils;

import com.google.gson.Gson;
import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Product;
import com.hufi.taxmanreader.model.Ticket;

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
