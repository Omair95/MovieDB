package com.example.moviedb.moviedb;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView tv = (TextView) findViewById(R.id.textViewInfo);
        tv.setText("Welcome to MovieDB!\n" +
                "\n" +
                "Your app to manage your films data.\n" +
                "\n" +
                "This is a little help to familiarize yourself with the functions of the application.\n" +
                "\n" +
                "When you open the app, the main screen will appear where you will find all the movies you have saved in the application sorted by title, if you click on any item in the list  you can access to the rest of the information, modify the note or delete the selected movie (will talk later about these options).\n" +
                "\n" +
                "Add Film:  (this option is accessible from the floating button of the main screen) displays a screen that allows to enter the data of a new movie: title, director, country, year of production and  protagonist. It also allows you to add a rating critics to the movie, by clicking on the stars that appear on the screen. By clicking on the green circular floating button you will add the new movie to the collection.\n" +
                "In the other hand if you finally decided not to add anything, click on the icon of the arrow in the upper left corner or the back key of your phone.\n" +
                "Remember that if any field is empty or  year is invalid a toast will appear showing the error and no film will be created neither the fields will be erased.\n" +
                "\n" +
                "Search Film: search for a movie by it's title. The search is extensive, allows you to search the movies that in its title contains our search. If we click on any item in the results list, we will access to the films data and options. To return to the main screen, press the back key or the <- icon in the upper left corner.\n" +
                "\n" +
                "Search Actor: Lets you search for the movies in which a particular protagonist participates. The search, as per title, allows searching for a string contained within the names of the protagonists. You can also access the movies that appear in the results by clicking on them and for return to the main screen, press the back key or the <- icon in the upper left corner.\n" +
                "\n" +
                "Remove Film (This option is also available on the data screen of each movie): If you select this option, it will allow you to delete one item from the list, clicking on it to select and delete.\n" +
                "\n" +
                "Modify critics (This option is also available in the data screen of each movie): It allows you to modify the rating critics assigned to a movie. Click on the number of stars you want to assign and then accept (cancel if you do not want to change anything).\n" +
                "\n" +
                "Sort by year: View the movies sorted by year of release. In addition, all your data appear in the list, so we can have a more specific view without having to enter each of the elements to check them.\n" +
                "\n" +
                "Sort by title: this is the initial view; a list of all the movies in the app, sorted alphabetically. Unlike the sorting by year, here only see the titles that are visualized to have a more compact list: it can facilitate the search if we have many elements (although if you have many elements, we recommend you to use the search options).\n" +
                "\n" +
                "Help: display this menu.\n" +
                "               \n" +
                "About: info about the creators of the app.\n" +
                "\n" +
                "Selected Film view: When selecting an element, we will see the information screen, where we can modify the score obtained by clicking on the pencil icon next to it and to delete the movie by clicking on the red floating button with the trash bin icon in the right-hand corner. To return to the list with all the movies, press the back key or the <- icon in the upper left corner.");
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
