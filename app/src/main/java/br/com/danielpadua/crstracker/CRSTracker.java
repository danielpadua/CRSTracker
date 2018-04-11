package br.com.danielpadua.crstracker;

import android.app.Application;
import android.content.Context;

public class CRSTracker extends Application {

    private static Context context;

    public static Context getAppContext() {
        return CRSTracker.context;
    }

    public void onCreate() {
        super.onCreate();
        CRSTracker.context = getApplicationContext();
    }
}