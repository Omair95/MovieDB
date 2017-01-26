package com.example.moviedb.moviedb;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.example.moviedb.moviedb.MainActivity.filmData;
import static com.example.moviedb.moviedb.MainActivity.filmList;
import static com.example.moviedb.moviedb.MainActivity.mAdapter;

public class SearchResultsActivity extends AppCompatActivity {

    StringAdapter mStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_search_results);

        final ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("myResults");

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        Collections.sort(myList);

        mStringAdapter = new StringAdapter(myList);
        recyclerView2.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mStringAdapter);

        recyclerView2.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView2, new MainActivity.ClickListener() {

            public void onClick(View view, int position) {
                boolean trobat = false;
                Film movie = new Film();
                for (int i = 0; i < filmList.size() && !trobat; ++i)
                {
                    if (filmList.get(i).getTitle().equals(myList.get(position)))
                    {
                        trobat = true;
                        movie = filmList.get(i);
                    }
                }
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(SearchResultsActivity.this, SelectedFilm.class);
                i.putExtra("FilmSelectedId", movie.getId());
                i.putExtra("FilmSelectedTitle", movie.getTitle());
                i.putExtra("FilmSelectedDirector", movie.getDirector());
                i.putExtra("FilmSelectedCountry", movie.getCountry());
                i.putExtra("FilmSelectedYear", movie.getYear());
                i.putExtra("FilmSelectedProta", movie.getProtagonist());
                i.putExtra("FilmSelectedCritica", movie.getCritics_rate());
                startActivity(i);
            }

            public void onLongClick(View view, int position) {

            }
        }));
    }

    public static class CustomComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return (o1.compareTo(o2));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), "** " + query, Toast.LENGTH_LONG).show();

            Cursor c = filmData.getWordMatches(query, null);
            //process Cursor and display results
            Toast.makeText(getApplicationContext(), "TROBAT", Toast.LENGTH_LONG).show();
        }
    }
}
