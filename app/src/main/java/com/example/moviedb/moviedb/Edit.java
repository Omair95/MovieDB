package com.example.moviedb.moviedb;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by edu on 4/01/17.
 */

public class Edit extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.floatcritics);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double w = 0.8 * dm.widthPixels;
        double h = 0.8 * dm.heightPixels;

        int width = (int) w;
        int height = (int) h;


        getWindow().setLayout(width,height);
    }
}
