package de.manuelclever.cinema.database.data.movie;

import de.manuelclever.cinema.database.query.PSQL.PSQLQMovie;
import de.manuelclever.cinema.database.query.Query;
import de.manuelclever.cinema.util.LogGenerator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class PSQLMovieReader implements MovieDataRead {
    DataSource datasource;

    public PSQLMovieReader(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getMovie(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQMovie.movieQueryWhereID(PSQLQMovie.MOVIE_SELECT_MOVIE), id);
    }

    @Override
    public String getMovieSimple(int id) {
        return Query.queryWhereOneInt(
                datasource, PSQLQMovie.movieQueryWhereID(PSQLQMovie.MOVIE_SELECT_SIMPLE), id);
    }

    @Override
    public String getMovieName(int id) {
        return Query.queryWhereOneInt(
                datasource,
                PSQLQMovie.MOVIE_SELECT_NAME + PSQLQMovie.WHERE + PSQLQMovie.MOVIE_PARAMETER_ID + PSQLQMovie.END,
                id);
    }

    @Override
    public String findMovieSimple(Movie searchContent) {
        return movieQueryWhereChainSimple(searchContent);
    }

    private String movieQueryWhereChainSimple(Movie searchContent) {
        PreparedStatement querySelectWhereChain = createPreparedWhereChain(searchContent);

        if(querySelectWhereChain != null) {
            try(ResultSet rs = querySelectWhereChain.executeQuery()) {

                if (rs.next()) {
                    String json = rs.getString(PSQLQMovie.TABLE_MOVIE);
                    querySelectWhereChain.close();
                    return json;
                }
            } catch(SQLException e) {
                try {
                    querySelectWhereChain.close();
                } catch (SQLException ignore) {}

                e.printStackTrace();
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            }
        }
        return null;
    }

    private PreparedStatement createPreparedWhereChain(Movie searchContent) {
        StringBuilder whereChainBuilder = new StringBuilder();
        final String OR = " OR ";

        boolean name = false; boolean genre = false; boolean year = false; boolean ageRestr = false;

        whereChainBuilder.append(" WHERE ");

        if(searchContent.getName() != null) {
            name = true;
            whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_NAME).append(OR);
            whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_ORIGINAL_NAME).append(OR);
            whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_TAGS).append(OR);
        }
        if(searchContent.getGenre() != null) {
            genre = true;
            List<Genre> genres = searchContent.getGenre();
            for(int i = 0; i < genres.size(); i++) {
                whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_GENRE).append(OR);
            }
        }
        if(searchContent.getYear() != 0) {
            year = true;
            whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_YEAR).append(OR);
        }
        if(searchContent.getAgeRestriction() != 0) {
            ageRestr = true;
            whereChainBuilder.append(PSQLQMovie.MOVIE_PARAMETER_AGE_RESTR).append(OR);
        }

        int sizeWhereChain = whereChainBuilder.length();
        int lastOrIndex = sizeWhereChain - 5;
        whereChainBuilder.delete(lastOrIndex, sizeWhereChain - 1);
        try(Connection conn = datasource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    PSQLQMovie.CREATE_JSON +
                            PSQLQMovie.FROM_START +
                            PSQLQMovie.MOVIE_SELECT_MOVIE +
                            PSQLQMovie.FROM +
                            PSQLQMovie.TABLE_MOVIE +
                            whereChainBuilder +
                            PSQLQMovie.FROM_END +
                            PSQLQMovie.AS +
                            PSQLQMovie.ROW +
                            PSQLQMovie.END)) {


            int index = 0;
            if (name) {
                preparedStatement.setString(index++, "*" + searchContent.getName() + "*");
                preparedStatement.setString(index++, "*" + searchContent.getName() + "*");
                preparedStatement.setString(index++, "*" + searchContent.getName() + "*");
            }
            if (genre) {
                List<Genre> genres = searchContent.getGenre();
                for(int i = 0; i < genres.size(); i++) {
                    String genreString = genres.get(i).toString();
                    preparedStatement.setString(index++, genreString);
                }
            }
            if (year) {
                preparedStatement.setInt(index++, searchContent.getYear());
            }
            if (ageRestr) {
                preparedStatement.setInt(index++, searchContent.getAgeRestriction());
            }
            return preparedStatement;
        } catch (SQLException e) {
            e.printStackTrace();
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }
}
