package com.hufi.taxmanreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.GroomApplication;

public class GroomUtils {

    public static boolean userConnected(){
        SharedPreferences sharedPreferences = GroomApplication.getContext().getSharedPreferences(GroomApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(GroomApplication.getContext().getString(R.string.yoshimi_token), "");

        return !token.equals("");
    }
}
