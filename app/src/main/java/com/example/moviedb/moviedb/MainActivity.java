package com.example.moviedb.moviedb;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static FilmData filmData;
    public static List<Film> filmList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static FilmAdapter mAdapter;
    public static StringAdapter TitleAdapter;
    boolean titleView = false;
    boolean eraseIsSelected = false;
    boolean editIsSelected = false;
    final String TAG = "States";
    private Menu menu;
    public static Integer lastId = 1;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        insertData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            public void onClick(View view, final int position) {

                final Film movie = filmList.get(position);

                if (eraseIsSelected) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirmation")
                            .setMessage("Are you sure that you want delete " + movie.getTitle() + " from this application?\nThis action is not recoverable")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    filmData.removeFilm(movie.getId());
                                    Toast.makeText(getApplicationContext(), "Film deleted!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, MainActivity.class);
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
                    eraseIsSelected = false;
                }
                else if (editIsSelected) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                    final View layout = inflater.inflate(R.layout.floatcritics, null);
                    final View layout2 = inflater.inflate(R.layout.activity_selected_film, null);
                    builder.setMessage("Choose new rating value for " + movie.getTitle())
                            .setTitle("Rate it!")
                            .setView(layout)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: Edit film rate on DB
                                    float rating = ((RatingBar) layout.findViewById(R.id.ratingBarEdit)).getRating();

                                    RatingBar ratingBar = (RatingBar) layout2.findViewById(R.id.ratingBarShowFilm);
                                    Log.d("TAG", "KKKKKKKKKKK = " + ratingBar.getRating());
                                    ratingBar.setRating((int) rating);

                                    filmData.removeFilm(movie.getId());
                                    filmData.createFilm(movie.getTitle(), movie.getDirector(), movie.getCountry(),
                                            movie.getYear(), movie.getProtagonist(), (int) rating);

                                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(i);
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
                    editIsSelected=false;
                }
                else {
                    //Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, SelectedFilm.class);
                    i.putExtra("FilmSelectedId", movie.getId());
                    i.putExtra("FilmSelectedTitle", movie.getTitle());
                    i.putExtra("FilmSelectedDirector", movie.getDirector());
                    i.putExtra("FilmSelectedCountry", movie.getCountry());
                    i.putExtra("FilmSelectedYear", movie.getYear());
                    i.putExtra("FilmSelectedProta", movie.getProtagonist());
                    i.putExtra("FilmSelectedCritica", movie.getCritics_rate());
                    startActivity(i);
                }

            }

            public void onLongClick(View view, int position) {
                Film movie = filmList.get(position);
                long id = movie.getId();
                String name = movie.getTitle();
                showFilterPopup(view, id, name);
            }

        }));

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B71C1C")));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 && fab.isShown())
                {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddFilm.class);

                startActivity(i);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showFilterPopup(View v, final long id, final String name) {
        PopupMenu popup = new PopupMenu(this, v);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.floating_options, popup.getMenu());

        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_item:



                        new AlertDialog.Builder( MainActivity.this)
                                .setTitle ( "Confirmation")
                                .setMessage("Are you sure that you want delete " + name + " from this application?\nThis action is not recoverable")
                                .setPositiveButton("Yeah!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: delete film on click
                                        filmData.removeFilm(id);

                                        Collections.sort(filmList, new MainActivity.customTitleComparator());
                                        mAdapter.notifyDataSetChanged();

                                        Toast.makeText(getApplicationContext(), name + " deleted from DB!", Toast.LENGTH_LONG).show();

                                        Intent i= new Intent(MainActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(i);

                                    }
                                })
                                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d ("AlertDialog", "Negative");
                                        //TODO: return to activity
                                    }
                                })
                                .show();


                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }


    private void insertData() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        filmData = new FilmData(this);
        filmData.open();
        //filmData.restartDB();
        if (filmData.getAllFilms().isEmpty()) {
            filmData.createFilm("Police Story 3: Super Cop","Stanley Tong", "Hong Kong", 2013, "Jackie Chan", 3);
            filmData.createFilm("City Hunter", "Gordon Chan", "Hong Kong", 1993, "Jackie Chan", 3);
            filmData.createFilm("Apocalypse Now", "Francis Ford Coppola", "USA", 1979, "Martin Sheen", 5);
            filmData.createFilm("Dirty Harry", "Don Siegel", "USA", 1971, "Clint Eastwood", 4);
        }

        titleView = true;
        filmList = filmData.getAllFilms();
        Collections.sort(filmList, new customTitleComparator());
        ArrayList<String> filmTitles = new ArrayList<>();
        for (int i = 0; i < filmList.size(); ++i)
        {
            filmTitles.add(i, filmList.get(i).getTitle());
        }
        TitleAdapter = new StringAdapter(filmTitles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(TitleAdapter);


        /*
        filmList = filmData.getAllFilms();

        Collections.sort(filmList, new customYearComparator());

        mAdapter = new FilmAdapter(filmList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        */
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        editIsSelected = false;
        eraseIsSelected = false;
        Log.d(TAG, "MainActivity: onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        editIsSelected = false;
        eraseIsSelected = false;
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hide(false);
        editIsSelected = false;
        eraseIsSelected = false;
        if (titleView)
        {
            filmList = filmData.getAllFilms();
            Collections.sort(filmList, new customTitleComparator());
            ArrayList<String> filmTitles = new ArrayList<>();
            for (int i = 0; i < filmList.size(); ++i)
            {
                filmTitles.add(i, filmList.get(i).getTitle());
            }
            TitleAdapter = new StringAdapter(filmTitles);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(TitleAdapter);
        }
        else
        {
            //Toast.makeText(MainActivity.this, "MainActivity onResume", Toast.LENGTH_SHORT).show();
            filmList = filmData.getAllFilms();
            Collections.sort(filmList, new customYearComparator());
            mAdapter = new FilmAdapter(filmList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }



        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        menu.findItem(R.id.action_search).setVisible(false);
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }

    public static class customYearComparator implements Comparator<Film> {
        @Override
        public int compare(Film o1, Film o2) {
            return (o1.getYear().compareTo(o2.getYear()));
        }
    }


    public static class customTitleComparator implements Comparator<Film> {
        @Override
        public int compare(Film o1, Film o2) {
            return (o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: http://www.androidbegin.com/tutorial/android-search-listview-using-filter/
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search)
        {

        }
        else if (id == R.id.Searchfilm)
        {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.Addfilm)
        {
            hide(false);
            eraseIsSelected = false;
            editIsSelected = false;
            Intent i = new Intent(MainActivity.this, AddFilm.class);

            startActivity(i);
        }
        else if (id == R.id.Searchfilm)
        {
            hide(true);
            eraseIsSelected = false;
            editIsSelected = false;
            menu.findItem(R.id.action_search).setVisible(true);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Type to find films");
            searchView.setFocusable(true);
            searchView.setIconified(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                private int textView;

                @Override
                public boolean onQueryTextSubmit(String query) {
                    ArrayList<String> results = new ArrayList<>();
                    int j = 0;
                    boolean trobat = false;
                    for (int i = 0; i < filmList.size(); ++i)
                    {
                        if (filmList.get(i).getTitle().toLowerCase().contains(query) ||
                                filmList.get(i).getTitle().toUpperCase().contains(query)) {
                            results.add(j, filmList.get(i).getTitle());
                            Log.d("TAG", "////////// " + results.size());
                            ++j;
                            trobat = true;
                        }
                    }
                    if (!trobat)
                    {
                        Intent i = new Intent(MainActivity.this, NoResultsActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                        Log.d("TAG", "******** - " + results.size());
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
        }
        else if (id == R.id.SearchActor)
        {
            hide(true);
            eraseIsSelected = false;
            editIsSelected = false;
            menu.findItem(R.id.action_search).setVisible(true);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Type protagonists names");
            searchView.setFocusable(true);
            searchView.setIconified(false);

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
                        Intent i = new Intent(MainActivity.this, NoResultsActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
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
        }
        else if (id == R.id.SortYear)
        {
            hide(false);
            menu.findItem(R.id.action_search).setVisible(false);
            eraseIsSelected = false;
            editIsSelected = false;
            titleView = false;
            filmList = filmData.getAllFilms();
            Collections.sort(filmList, new customYearComparator());
            mAdapter = new FilmAdapter(filmList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
        }
        else if (id == R.id.SortTitle)
        {
            hide(false);
            menu.findItem(R.id.action_search).setVisible(false);
            eraseIsSelected = false;
            editIsSelected = false;
            titleView = true;
            filmList = filmData.getAllFilms();
            Collections.sort(filmList, new customTitleComparator());
            ArrayList<String> filmTitles = new ArrayList<>();
            for (int i = 0; i < filmList.size(); ++i)
            {
                filmTitles.add(i, filmList.get(i).getTitle());
            }
            TitleAdapter = new StringAdapter(filmTitles);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(TitleAdapter);
        }
        else if (id == R.id.RemoveFilmsNavigation){
            hide(false);
            Toast.makeText(MainActivity.this, "Please, select film to erase", Toast.LENGTH_LONG).show();
            //recyclerView.setBackgroundColor();
            eraseIsSelected = true;
            editIsSelected = false;
            menu.findItem(R.id.action_search).setVisible(false);
            //select film to remove
                //popup
        }
        else if (id == R.id.EditFilmNavigation){
            hide(false);
            Toast.makeText(MainActivity.this, "Please, select film to edit", Toast.LENGTH_LONG).show();
            editIsSelected = true;
            eraseIsSelected = false;
            menu.findItem(R.id.action_search).setVisible(false);
            //select film to edit
                //popup

        }
        else if (id == R.id.info) {
            hide(false);
            eraseIsSelected = false;
            editIsSelected = false;
            Intent i = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(i);
        }
        else if (id == R.id.about) {
            hide(false);
            eraseIsSelected = false;
            editIsSelected = false;
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void SendReport(View v)
    {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.bugs_alert, null, false);
        EditText text = (EditText) view.findViewById(R.id.editText4);
        String report = text.getText().toString();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Omair95@protonmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "MovieDB-Report");
        i.putExtra(Intent.EXTRA_TEXT   , report);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public void hide(boolean hide) {
        recyclerView.setVisibility(hide ? View.GONE : View.VISIBLE);
    }
}
