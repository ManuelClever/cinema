package de.manuelclever.cinema.database.data.movie;

public interface MovieDataRead {
    String getMovie(int movieID);
    String getMovieSimple(int movieID);
    String getMovieName(int movieID);
    String findMovieSimple(Movie searchContent);
}
