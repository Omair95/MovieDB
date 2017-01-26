package com.example.moviedb.moviedb;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;

import static com.example.moviedb.moviedb.MainActivity.filmList;

public class SearchActorActivity extends AppCompatActivity {

    ActorsAdapter mStringAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_actor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Type to find films");
        searchView.setFocusable(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private int textView;

            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<String> results = new ArrayList<>();
                int j = 0;
                boolean trobat = false;
                for (int i = 0; i < filmList.size(); ++i)
                {
                    if (filmList.get(i).getProtagonist().toLowerCase().contains(query) ||
                            filmList.get(i).getProtagonist().toUpperCase().contains(query)) {
                        results.add(j, filmList.get(i).getTitle());
                        ++j;
                        trobat = true;
                    }
                }

                if (!trobat)
                {
                    Intent i = new Intent(SearchActorActivity.this, NoResultsActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SearchActorActivity.this, SearchResultsActivity.class);
                    i.putExtra("myResults", results);
                    startActivity(i);
                }

                searchView.setQuery(query, false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }

        });

        return true;
    }
}
