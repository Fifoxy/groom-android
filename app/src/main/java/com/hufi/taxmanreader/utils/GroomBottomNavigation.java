package com.hufi.taxmanreader.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.hufi.taxmanreader.R;
import com.hufi.taxmanreader.activities.MainActivity;

/**
 * Created by pierredfc.
 */

public class GroomBottomNavigation implements AHBottomNavigation.OnTabSelectedListener {

    private Activity callback;
    private Toolbar toolbar;
    private Bundle savedInstanceState;
    private AHBottomNavigation navigation;

    public static final int NAVIGATION_ITEM_HOME = 0;
    private AHBottomNavigationItem navigationItemHome;
    public static final int NAVIGATION_ITEM_SCAN = 1;
    private AHBottomNavigationItem navigationItemScan;
    public static final int NAVIGATION_ITEM_ACCOUNT = 2;
    private AHBottomNavigationItem navigationItemAccount;


    public GroomBottomNavigation(AHBottomNavigation navigation, Bundle savedInstanceState, Activity activity, Toolbar toolbar) {
        this.navigation = navigation;
        this.savedInstanceState = savedInstanceState;
        this.callback = activity;
        this.toolbar = toolbar;

        this.initNavigation();
        this.navigation.setOnTabSelectedListener(this);
    }

    public void initNavigation()
    {
        // Create items
        this.navigationItemHome = new AHBottomNavigationItem(R.string.bnav_home, R.drawable.ic_home_white_24dp, R.color.colorAccent);
        this.navigationItemScan = new AHBottomNavigationItem(R.string.bnav_scan, R.drawable.ic_action_camera, R.color.colorAccent);
        this.navigationItemAccount = new AHBottomNavigationItem(R.string.bnav_account, R.drawable.ic_account_circle_white_24dp, R.color.colorAccent);

        // Add items
        this.navigation.addItem(this.navigationItemHome);
        this.navigation.addItem(this.navigationItemScan);
        this.navigation.addItem(this.navigationItemAccount);

        this.navigation.setAccentColor(R.color.colorAccent);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected)
    {
        return !wasSelected ; //&& this.callback instanceof MainActivity && ((MainActivity) this.callback).onTabSelected(position);
    }

    public interface GroomBottomNavigationCallback
    {
        boolean onTabSelected(int position);
    }
}