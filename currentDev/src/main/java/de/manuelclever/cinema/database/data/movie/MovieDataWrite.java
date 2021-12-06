package de.manuelclever.cinema.database.data.movie;

public interface MovieDataWrite {
    int addMovie(Movie movie);
    int updateMovie(Movie movie);
    boolean removeMovie(int movieID);
}
