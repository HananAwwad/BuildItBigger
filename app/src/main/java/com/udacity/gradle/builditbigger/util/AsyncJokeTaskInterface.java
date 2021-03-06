package com.udacity.gradle.builditbigger.util;

import android.app.Activity;
import android.os.Handler;

import com.hananawwad.androidjoker.Joke;


public interface AsyncJokeTaskInterface {
    Activity getActivity();
    Handler getHandler();
    void showCircleView();
    void setCircleViewValue(final float value);
    void passJokeFromJavaLibraryToAndroidLibrary(Joke aJoke);
}

