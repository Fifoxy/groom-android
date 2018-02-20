package fr.galaisen.groomreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import fr.galaisen.groomreader.R;
import fr.galaisen.groomreader.GroomApplication;

public class GroomUtils {

    public static boolean userConnected(){
        SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(GroomApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(GroomApplication.getContext().getString(R.string.yoshimi_token), "");

        return !token.equals("");
    }
}
