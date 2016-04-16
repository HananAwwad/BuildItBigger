package com.udacity.gradle.builditbigger.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hananawwad.androidjoker.Joke;
import com.hananawwad.androidjoker.JokeActivity;
import com.udacity.gradle.builditbigger.R;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;


@SuppressLint("Registered")
public class AppCompatCircleViewActivityBase extends AppCompatActivity implements AsyncJokeTaskInterface {

    private final static String TAG = "LEE: <" + AppCompatCircleViewActivityBase.class.getSimpleName() + ">";

    private Handler mHandler;
    private CircleProgressView mCircleView;
    private boolean mShowUnit;
    private Joke theJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mShowUnit = false;
    }

    public void showCircleView() {

        mCircleView = (CircleProgressView) findViewById(R.id.circle_view);
        if (mCircleView == null) {
            Log.w(TAG, "showCircleView: CircleProgressView problem - mCircleView is null!");
            return;
        }
        initializeCircleViewMaxValue(3);

        mCircleView.post(new Runnable() {
            @Override
            public void run() {

                mShowUnit = false;
                Log.w(TAG, "showCircleView");
                mCircleView.setAutoTextSize(true); // enable auto text size, previous values are overwritten
                mCircleView.setUnitScale(0.9f); // if you want the calculated text sizes to be bigger/smaller
                mCircleView.setTextScale(0.9f); // if you want the calculated text sizes to be bigger/smaller
                mCircleView.setTextColor(Color.RED);
                mCircleView.setText("Loading.."); //shows the given text in the circle view
                mCircleView.setTextMode(TextMode.TEXT); // Set text mode to text to show text
                mCircleView.spin(); // start spinning
                mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode

                mCircleView.setAnimationStateChangedListener(

                        new AnimationStateChangedListener() {
                            @Override
                            public void onAnimationStateChanged(AnimationState _animationState) {
                                if (mCircleView != null) {
                                    switch (_animationState) {
                                        case IDLE:
                                        case ANIMATING:
                                        case START_ANIMATING_AFTER_SPINNING:
                                            mCircleView.setTextMode(TextMode.PERCENT); // show percent if not spinning
                                            mCircleView.setShowUnit(mShowUnit);
                                            break;
                                        case SPINNING:
                                            mCircleView.setTextMode(TextMode.TEXT); // show text while spinning
                                            mCircleView.setShowUnit(false);
                                        case END_SPINNING:
                                            break;
                                        case END_SPINNING_START_ANIMATING:
                                            break;

                                    }
                                }
                            }
                        }

                );

                mCircleView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initializeCircleViewMaxValue(@SuppressWarnings("SameParameterValue") final float maxValue) {
        if (mCircleView != null) {
            Log.w(TAG, "initializeCircleViewMaxValue: CircleView MAX=" + maxValue);

            mCircleView.post(new Runnable() {
                @Override
                public void run() {
                    mShowUnit = true;
                    mCircleView.setVisibility(View.INVISIBLE);
                    mCircleView.setValue(0);
                    mCircleView.setUnit("%");
                    mCircleView.setShowUnit(mShowUnit);
                    mCircleView.setTextMode(TextMode.PERCENT);
                    mCircleView.setMaxValue(maxValue);
                }
            });

        }
    }

    public void setCircleViewValue(final float value) {
        if (mCircleView != null) {
            Log.w(TAG, "setCircleViewValue: CircleView value=" + value);

            mCircleView.post(new Runnable() {
                @Override
                public void run() {
                    mCircleView.setValue(value);
                }
            });

        }
    }

    @Override
    public void passJokeFromJavaLibraryToAndroidLibrary(final Joke aJoke) {
        Log.w(TAG, "passJokeFromJavaLibraryToAndroidLibrary");
        theJoke = aJoke;
        if (mCircleView != null) {
            final int mDelay = 1000;
            final AppCompatCircleViewActivityBaseInterface superActivity = (AppCompatCircleViewActivityBaseInterface) this;
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.w(TAG, "===> makeVisible <===");
                    superActivity.makeVisible();
                }
            }, mDelay * 3);

            final String from = superActivity.getClass().getCanonicalName();
            final Activity activity = this;
            mCircleView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCircleView != null) {
                        mCircleView.stopSpinning();
                        mCircleView.setVisibility(View.INVISIBLE);
                    }

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.putExtra("JOKE", theJoke.getJoke());
                            intent.putExtra("FROM", from);
                            intent.setClass(activity, JokeActivity.class);
                            startActivity(intent);
                        }
                    }, mDelay / 2);

                }
            }, mDelay / 2);

        }
    }


    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        mCircleView = null;
        mHandler = null;
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public Joke getTheJoke() {
        return theJoke;
    }

}
