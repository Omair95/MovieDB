package com.example.moviedb.moviedb;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.security.AccessController.getContext;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.MyViewHolder> {

    private List<Film> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, year, director, prota;
        public RatingBar rate;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.rowTitle);
            year = (TextView) view.findViewById(R.id.rowYear);
            director = (TextView) view.findViewById(R.id.rowDirector);
            prota = (TextView) view.findViewById(R.id.rowProta);
            rate = (RatingBar) view.findViewById(R.id.rowRatingBar);
        }

        @Override
        public void onClick(View view) {
            Log.d("HOLA", "onClick " + "*************************" + " ");
        }

    }


    public FilmAdapter(List<Film> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Film movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(new String(Integer.toString(movie.getYear())) + ", " + movie.getCountry());
        holder.director.setText("by " + movie.getDirector());
        holder.prota.setText("Starring: " + movie.getProtagonist());
        holder.rate.setRating(movie.getCritics_rate());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}