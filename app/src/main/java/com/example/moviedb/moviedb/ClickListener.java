package com.example.moviedb.moviedb;

import android.view.View;

/**
 * Created by omair on 22/12/16.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}