package de.manuelclever.cinema.database.data.movie;

import de.manuelclever.cinema.database.query.PSQL.PSQLQMovie;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class PSQLMovieWriter implements MovieDataWrite {
    DataSource datasource;

    public PSQLMovieWriter(DataSource datasource) {
        this.datasource = datasource;
    }

        @Override
    public int addMovie(Movie movie) {
        if(movie.getId() == 0) {
            try (Connection conn = datasource.getConnection();
                 PreparedStatement queryInsertMovie = prepareMovieStatement(conn, PSQLQMovie.MOVIE_QUERY_INSERT, movie)) {

                if (queryInsertMovie != null) {
                    ResultSet rs = queryInsertMovie.executeQuery();

                    if (rs.next()) {
                        conn.commit();
                        return rs.getInt(PSQLQMovie.MOVIE_ID);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            }
        } else {
            return updateMovie(movie);
        }
        return 0;
    }

    private PreparedStatement prepareMovieStatement(Connection conn, String sql, Movie movie) throws SQLException{
        try {
            PreparedStatement queryInsertMovie = conn.prepareStatement(sql);

            queryInsertMovie.setString(1, movie.getName());
            queryInsertMovie.setString(2, movie.getOriginalName());
            queryInsertMovie.setString(3, movie.getGenre().toString());
            queryInsertMovie.setString(4, movie.getDescription());
            queryInsertMovie.setInt(5, movie.getLength());
            queryInsertMovie.setString(6, movie.getOther().toString());
            queryInsertMovie.setString(7, movie.getActors().toString());
            queryInsertMovie.setString(8, movie.getDirectors().toString());
            queryInsertMovie.setString(9, movie.getCountry());
            queryInsertMovie.setInt(10, movie.getYear());
            queryInsertMovie.setInt(11, movie.getAgeRestriction());
            queryInsertMovie.setString(12, movie.getStudio());
            queryInsertMovie.setString(13, movie.getTrailer().toString());
            queryInsertMovie.setString(14, movie.getTags().toString());

            return queryInsertMovie;
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }

    @Override
    public int updateMovie(Movie movie) {
        try(Connection conn = datasource.getConnection();
            PreparedStatement queryUpdate =
                    prepareMovieStatement(conn, PSQLQMovie.MOVIE_QUERY_UPDATE_WHERE_IP, movie)) {


            if(queryUpdate != null) {
                queryUpdate.setInt(13, movie.getId());

                ResultSet resultSet = queryUpdate.executeQuery();
                if(resultSet.next()) {
                    conn.commit();
                    return resultSet.getInt(PSQLQMovie.MOVIE_ID);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean removeMovie(int movieID) {
        try(Connection conn = datasource.getConnection();
             PreparedStatement queryDeleteMovie = conn.prepareStatement(PSQLQMovie.MOVIE_QUERY_DELETE)){

            queryDeleteMovie.setInt(1, movieID);

            boolean success = queryDeleteMovie.execute();
            conn.commit();
            return success;
        } catch(SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return false;
    }
}
