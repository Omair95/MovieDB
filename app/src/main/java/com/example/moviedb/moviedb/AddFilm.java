package com.example.moviedb.moviedb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Collections;
import java.util.Locale;

import static com.example.moviedb.moviedb.MainActivity.filmData;
import static com.example.moviedb.moviedb.MainActivity.filmList;
import static com.example.moviedb.moviedb.MainActivity.mAdapter;

public class AddFilm extends AppCompatActivity {

    final String TAG = "States";


    public boolean dataIsCorrect()
    {
         EditText NomPelicula = (EditText)findViewById(R.id.editTitle);
         EditText NomDirector = (EditText)findViewById(R.id.editDirector);
         EditText PaisPelicula   = (EditText)findViewById(R.id.editCountry);
         EditText AnyPelicula = (EditText)findViewById(R.id.editYear);
         EditText NomProta = (EditText)findViewById(R.id.editProta);
         RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

         String nom_pelicula = NomPelicula.getText().toString();
         String director_pelicula = NomDirector.getText().toString();
         String pais_pelicula = PaisPelicula.getText().toString();
         String prota_pelicula = NomProta.getText().toString();
         float valor_critica = ratingBar.getRating();

         boolean correcte = false;
         if (nom_pelicula.equals(""))
         {
         Toast.makeText(getApplicationContext(), "Field title can't be empty", Toast.LENGTH_SHORT).show();
         }
         else if (director_pelicula.equals(""))
         {
         Toast.makeText(getApplicationContext(), "Field director's name can't be empty", Toast.LENGTH_SHORT).show();
         }
         else if (pais_pelicula.equals(""))
         {
         Toast.makeText(getApplicationContext(), "Field country can't be empty", Toast.LENGTH_SHORT).show();
         }
         else if (AnyPelicula.getText().toString().equals(""))
         {
         Toast.makeText(getApplicationContext(), "Field year can't be empty", Toast.LENGTH_SHORT).show();
         }
         else if (Integer.parseInt(AnyPelicula.getText().toString()) < 1800)
         {
         Toast.makeText(getApplicationContext(), "Year must be a valid value (Greater than 1800)", Toast.LENGTH_SHORT).show();
         }
         else if (Integer.parseInt(AnyPelicula.getText().toString()) > 2017)
         {
         Toast.makeText(getApplicationContext(), "Year must be a valid value (Lesser than actual year)", Toast.LENGTH_SHORT).show();
         }
         else if (prota_pelicula.equals(""))
         {
         Toast.makeText(getApplicationContext(), "Field protagonist can't be empty", Toast.LENGTH_SHORT).show();
         }
         else {
             return true;
         }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(AddFilm.this, MainActivity.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    public void addFilm()
    {
         EditText NomPelicula = (EditText)findViewById(R.id.editTitle);
         EditText NomDirector = (EditText)findViewById(R.id.editDirector);
         EditText PaisPelicula   = (EditText)findViewById(R.id.editCountry);
         EditText AnyPelicula = (EditText)findViewById(R.id.editYear);
         EditText NomProta = (EditText)findViewById(R.id.editProta);
         RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

         String nom_pelicula = NomPelicula.getText().toString();
         String director_pelicula = NomDirector.getText().toString();
         String pais_pelicula = PaisPelicula.getText().toString();
         String prota_pelicula = NomProta.getText().toString();
         float valor_critica = ratingBar.getRating();

         Integer any_pelicula = Integer.parseInt(AnyPelicula.getText().toString());
         Film movie = new Film();
         movie.setTitle(nom_pelicula);
         movie.setDirector(director_pelicula);
         movie.setCountry(pais_pelicula);
         movie.setYear(any_pelicula);
         movie.setProtagonist(prota_pelicula);
         movie.setCritics_rate((int) valor_critica);

         filmData.createFilm(nom_pelicula, director_pelicula, pais_pelicula, any_pelicula, prota_pelicula, (int) valor_critica);

         Toast.makeText(getApplicationContext(), "Film added", Toast.LENGTH_LONG).show();

         Intent i= new Intent(AddFilm.this, MainActivity.class);
         i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
         startActivity(i);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_film);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#388E3C")));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    EditText NomPelicula = (EditText)findViewById(R.id.editTitle);
                    String nom_pelicula = NomPelicula.getText().toString();
                    new AlertDialog.Builder(AddFilm.this)
                            .setTitle("Confirmation")
                            .setMessage("Are you sure that you want to add " + nom_pelicula + " to the list?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //Collections.sort(filmList, new MainActivity.CustomComparator());
                                    //mAdapter.notifyDataSetChanged();
                                    addFilm();
                                    Toast.makeText(getApplicationContext(), "Film deleted!", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(AddFilm.this, MainActivity.class);
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
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "ActivityTwo: onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "ActivityTwo: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "ActivityTwo: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "ActivityTwo: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "ActivityTwo: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ActivityTwo: onDestroy()");
    }

}
