package com.example.moviedb.moviedb;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Collections;

import static com.example.moviedb.moviedb.MainActivity.filmData;
import static com.example.moviedb.moviedb.MainActivity.filmList;
import static com.example.moviedb.moviedb.MainActivity.mAdapter;
import static com.example.moviedb.moviedb.R.layout.floatcritics;
import static com.example.moviedb.moviedb.R.layout.layout_tab_text;

public class SelectedFilm extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_film);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        final long id = getIntent().getLongExtra("FilmSelectedId", 0);
        final String title = getIntent().getStringExtra("FilmSelectedTitle");
        String Director = getIntent().getStringExtra("FilmSelectedDirector");
        String Pais = getIntent().getStringExtra("FilmSelectedCountry");
        int Año = getIntent().getIntExtra("FilmSelectedYear", 0);
        String Prota = getIntent().getStringExtra("FilmSelectedProta");
        final int Critics = getIntent().getIntExtra("FilmSelectedCritica", 0);

        FloatingActionButton deleteBtn = (FloatingActionButton) findViewById(R.id.deleteFloating);
        Button editBtn = (Button) findViewById(R.id.editBtn);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectedFilm.this);
                LayoutInflater inflater = SelectedFilm.this.getLayoutInflater();
                final View layout = inflater.inflate(R.layout.floatcritics, null);
                builder.setMessage("Choose new rating value")
                        .setTitle("Rate it!")
                        .setView(layout)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Edit film rate on DB
                                float rating = ((RatingBar) layout.findViewById(R.id.ratingBarEdit)).getRating();

                                RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBarShowFilm);
                                Log.d("TAG", "MMMMMMMMMMM = " + ratingBar.getRating());
                                ratingBar.setRating((int) rating);


                                Film movie = filmData.buscaFilm(id);
                                filmData.removeFilm(id);

                                filmData.createFilm(movie.getTitle(), movie.getDirector(), movie.getCountry(),
                                        movie.getYear(), movie.getProtagonist(), (int) rating);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog","Negative");
                            }
                        })
                        .setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SelectedFilm.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure that you want delete " + title + " from this application?\nThis action is not recoverable")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filmData.removeFilm(id);

                                //Collections.sort(filmList, new MainActivity.CustomComparator());
                                //mAdapter.notifyDataSetChanged();

                                Toast.makeText(getApplicationContext(), "Film deleted!", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(SelectedFilm.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("No!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Negative");
                            }
                        })
                        .show();
            }
        });


        TextView FilmTitle = (TextView) findViewById(R.id.textViewTitleName);
        FilmTitle.setText(title);

        TextView FilmDirector = (TextView) findViewById(R.id.textViewDirectorName);
        FilmDirector.setText("by " + Director);

        TextView FilmPais = (TextView) findViewById(R.id.textViewCountryYear);
        FilmPais.setText(Pais + " ," + Integer.toString(Año));

        TextView FilmProta = (TextView) findViewById(R.id.textViewProtaName);
        FilmProta.setText("Starring by: " + Prota);

        RatingBar rating = (RatingBar) findViewById(R.id.ratingBarShowFilm);
        rating.setNumStars(5);
        rating.setRating(Critics);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SelectedFilm Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
