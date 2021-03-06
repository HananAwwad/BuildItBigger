package com.hananawwad.androidjoker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class JokeActivity extends AppCompatActivity {
    private final static String TAG = JokeActivity.class.getSimpleName();

    private TextView mJokeView;
    private Button mTellAnotherJokeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate - setContentView(activity_joke)");
        setContentView(R.layout.activity_joke);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent theIntent = getIntent();

        mTellAnotherJokeButton = (Button) findViewById(R.id.another_joke);
        mJokeView = (TextView) findViewById(R.id.joke_text);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        } else {
            String jokeText = theIntent.getStringExtra("JOKE");
            if (jokeText == null) {
                jokeText = getResources().getString(R.string.no_joke);
            }
            Log.v(TAG, "jokeText=" + jokeText);
            mJokeView.setText(jokeText);
        }

        String from = theIntent.getStringExtra("FROM");
        if (from != null) {
            Log.v(TAG, "ok to TELL ANOTHER JOKE - name of the invoking class: FROM=" + from);
            mTellAnotherJokeButton.setVisibility(View.VISIBLE);
        } else {
            Log.v(TAG, "missing name of the invoking class - TELL ANOTHER BUTON IS INVISIBLE");
            mTellAnotherJokeButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String joke = savedInstanceState.getString("JOKE");
        Log.v(TAG, "onRestoreInstanceState - joke=" + joke);
        mJokeView.setText(joke);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "saving instance state");
        super.onSaveInstanceState(outState);
        outState.putString("JOKE", mJokeView.getText().toString());
    }

    @SuppressWarnings("unused")
    public void tellAnotherJoke(@SuppressWarnings("UnusedParameters") View view) {
        Log.v(TAG, "tellAnotherJoke");
        mJokeView.setText(getResources().getString(R.string.empty));
        mTellAnotherJokeButton.setVisibility(View.INVISIBLE);
        Intent theIntent = getIntent();
        String fromClass = theIntent.getStringExtra("FROM");
        if (fromClass == null) {
            Log.v(TAG, "unable to tell another joke - 'FROM' missing from intent");

        } else {
            try {
                Log.v(TAG, "Start Activity from CLASS=" + fromClass);
                Class<?> backToClass = Class.forName(fromClass);
                Intent tellAnotherIntent = new Intent(this, backToClass);
                tellAnotherIntent.putExtra("FROM", JokeActivity.class.getCanonicalName());
                tellAnotherIntent.putExtra("TELL_ANOTHER_JOKE", "true");
                tellAnotherIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tellAnotherIntent);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "UNABLE TO LOCATE CLASS" + fromClass);
            }
        }
    }

}
