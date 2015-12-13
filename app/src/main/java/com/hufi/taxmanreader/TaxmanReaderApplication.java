package com.hufi.taxmanreader;

import android.app.Application;
import android.content.Context;

/**
 * Created by Pierre Defache on 12/12/2015.
 */
public class TaxmanReaderApplication extends Application {
    private static Context sContext;

    public void onCreate(){
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
