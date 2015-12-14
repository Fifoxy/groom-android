package com.hufi.taxmanreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.TaxmanReaderApplication;

/**
 * Created by Pierre Defache on 14/12/2015.
 */
public class TaxmanUtils {

    public static boolean userConnected(){
        SharedPreferences sharedPreferences = TaxmanReaderApplication.getContext().getSharedPreferences(TaxmanReaderApplication.getContext().getString(R.string.yoshimi), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(TaxmanReaderApplication.getContext().getString(R.string.yoshimi_token), "");

        return !token.equals("");
    }
}
