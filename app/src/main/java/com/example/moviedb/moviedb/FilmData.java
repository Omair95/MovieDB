package com.example.moviedb.moviedb;

/**
 * FilmData
 * Created by pr_idi on 10/11/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FilmData {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    // Here we only select Title and Director, must select the appropriate columns
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_DIRECTOR, MySQLiteHelper.COLUMN_COUNTRY,
            MySQLiteHelper.COLUMN_YEAR_RELEASE, MySQLiteHelper.COLUMN_PROTAGONIST, MySQLiteHelper.COLUMN_CRITICS_RATE};

    public FilmData(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public Cursor getWordMatches(String query, String[] columns) {
        String selection = MySQLiteHelper.COLUMN_TITLE + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {

        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILMS,
                columns, selection, selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        Film newFilm = cursorToFilm(cursor);

        return cursor;
    }

    public void close() {
        dbHelper.close();
    }

    public long createFilm(String title, String director, String country, int any, String protagonista, int critics) {
        ContentValues values = new ContentValues();
        Log.d("Creating", "Creating " + title + " " + director);

        // Add data: Note that this method only provides title and director
        // Must modify the method to add the full data
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_DIRECTOR, director);
        values.put(MySQLiteHelper.COLUMN_COUNTRY, country);
        values.put(MySQLiteHelper.COLUMN_YEAR_RELEASE, any);
        values.put(MySQLiteHelper.COLUMN_PROTAGONIST, protagonista);
        values.put(MySQLiteHelper.COLUMN_CRITICS_RATE, critics);

        // Actual insertion of the data using the values variable
        long insertId = database.insert(MySQLiteHelper.TABLE_FILMS, null,
                values);

        // Return the book
        return insertId;
    }

    public void editFilmRate(int id, int newRate)
    {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILMS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Film film = cursorToFilm(cursor);
            if (film.getId() == id){
                //TODO: modificar critica
            }
        }
    }

    public List<Film> getAllFilms() {
        List<Film> comments = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Film comment = cursorToFilm(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    public ArrayList<String> getAllFilmsName() {
        ArrayList<String> results = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Film comment = cursorToFilm(cursor);
            results.add(comment.getTitle());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return results;
    }

    public void restartDB()
    {
        dbHelper.onUpgrade(database, 0, 1);
    }

    private Film cursorToFilm(Cursor cursor) {
        Film film = new Film();
        film.setId(cursor.getLong(0));
        film.setTitle(cursor.getString(1));
        film.setDirector(cursor.getString(2));
        film.setCountry(cursor.getString(3));
        film.setYear(cursor.getInt(4));
        film.setProtagonist(cursor.getString(5));
        film.setCritics_rate(cursor.getInt(6));
        return film;
    }

    public Film buscaFilm(long id)
    {
        // Main activity calls this procedure to create a new film
        // and uses the result to update the listview.
        // Therefore, we need to get the data from the database
        // (you can use this as a query example)
        // to feed the view.

        Cursor cursor = database.query(MySQLiteHelper.TABLE_FILMS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);
        cursor.moveToFirst();
        Film newFilm = cursorToFilm(cursor);

        // Do not forget to close the cursor
        cursor.close();

        return newFilm;
    }

    public void removeFilm(long id)
    {
        database.delete(MySQLiteHelper.TABLE_FILMS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }
}