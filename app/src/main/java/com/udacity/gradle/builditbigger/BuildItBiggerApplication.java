package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;


@SuppressWarnings("unused")
public class BuildItBiggerApplication extends Application {
    private final static String TAG = BuildItBiggerApplication.class.getSimpleName() ;

    private static Context sContext;
    private static BuildItBiggerApplication mInstance;

    public void onCreate() {
        Log.v(TAG, "onCreate");
        mInstance = this;
        super.onCreate();
        BuildItBiggerApplication.sContext = getApplicationContext();
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }

    public static Context getAppContext() {
        Log.v(TAG, "getAppContext");
        return BuildItBiggerApplication.sContext;
    }

    public static synchronized BuildItBiggerApplication getInstance() {
        Log.v(TAG, "getInstance");
        return BuildItBiggerApplication.mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }


    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        t.setScreenName(screenName);

        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }


    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

}

