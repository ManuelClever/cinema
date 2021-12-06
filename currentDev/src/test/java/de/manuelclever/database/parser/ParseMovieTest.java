package de.manuelclever.database.parser;

import de.manuelclever.cinema.database.data.movie.Genre;
import de.manuelclever.cinema.database.data.movie.Movie;
import de.manuelclever.cinema.database.data.movie.StandardMovie;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseMovieTest {

    @Test
    public void withAllInput() {
        Movie movie = new StandardMovie("Herr der Ringe", "Lord Of The Rings");
        movie.addGenre(Genre.ACTION, Genre.ADVENTURE);
        movie.setDescription("Ein Ring sie zu knechten.");
        movie.setLength(140);
        movie.addOther("3D", "4K", "48FPS");
        movie.addActor("Keine Ahnung", "Schauspielername");
        movie.addDirector("Peter Jackson");
        movie.setYear(2001);
        movie.setAgeRestriction(12);
        movie.setStudio("Studio");
        movie.addTrailer("https://youtube.com/trailer1", "https://youtube.com/trailer2");
        movie.addTag("hobbit", "lord of flies", "herr der fliegen", "ring", "gollum");


        String json = "[{\"movie_id\":\"\"," +
                "\"name\":\"Herr der Ringe\"," +
                "\"original_name\":\"lord of the rings\"," +
                "\"genre\":\"action,adventure\"," +
                "\"description\":\"Ein Ring sie zu knechten.\"," +
                "\"length\":140," +
                "\"other\":\"3D,4K,48FPS\"," +
                "\"actors\":\"Keine Ahnung,Schauspielername\"," +
                "\"directors\":\"Peter Jackson\"," +
                "\"year\":2001," +
                "\"age_restriction\":12, " +
                "\"studio\":\"Studio\"," +
                "\"trailer\":\"https://youtube.com/trailer1,https://youtube.com/trailer2\"," +
                "\"tags\":\"hobbit,lord of flies,herr der fliegen,ring,gollum\"}]";

        Assertions.assertEquals(movie, MyJsonParser.parseMovie(json));
    }

    @Test
    public void withMandatoryInput() {
        Movie movie = new StandardMovie("Herr der Ringe", "Lord Of The Rings");
        movie.addGenre(Genre.ACTION, Genre.ADVENTURE);
        movie.setDescription("Ein Ring sie zu knechten.");


        String json =  "[{\"movie_id\":\"\"," +
                "\"name\":\"Herr der Ringe\"," +
                "\"original_name\":\"lord of the rings\"," +
                "\"genre\":\"action,adventure\"," +
                "\"description\":\"Ein Ring sie zu knechten.\"," +
                "\"length\":\"\"," +
                "\"other\":\"\"," +
                "\"actors\":\"\"," +
                "\"directors\":\"\"," +
                "\"year\":\"\"," +
                "\"age_restriction\":\"\", " +
                "\"studio\":\"\"," +
                "\"trailer\":\"\"," +
                "\"tags\":\"\"}]";

        Assertions.assertEquals(movie, MyJsonParser.parseMovie(json));
    }

    @Test
    public void withoutMandatoryInputOfTypeString() {
        Movie movie = new StandardMovie();

        String json =  "[{\"movie_id\":\"\"," +
                "\"name\":\"\"," +
                "\"original_name\":\"\"," +
                "\"genre\":\"\"," +
                "\"description\":\"\"," +
                "\"length\":\"\"," +
                "\"other\":\"\"," +
                "\"actors\":\"\"," +
                "\"directors\":\"\"," +
                "\"year\":\"\"," +
                "\"age_restriction\":\"\", " +
                "\"studio\":\"\"," +
                "\"trailer\":\"\"," +
                "\"tags\":\"\"}]";

        Assertions.assertEquals(movie, MyJsonParser.parseMovie(json));
    }

//    @ParameterizedTest
//    @MethodSource()
//    public void wrongInputType(String json) {
//        Assertions.assertNull(MyJsonParser.parseMovie(json));
//    }
//
//    private static Stream<String> wrongInputType() {
//        List<String> jsonList = new ArrayList<>();
//
//        String year = "[{\"name\":\"Herr der Ringe\"," +
//                "\"original_name\":\"lord of the rings\"," +
//                "\"genre\":{\"action\",\"adventure\"}," +
//                "\"description\":\"Ein Ring sie zu knechten.\"," +
//                "\"length\":\"\"," +
//                "\"other\":\"\"," +
//                "\"actors\":\"\"," +
//                "\"directors\":\"\"," +
//                "\"year\":\"text\"," +
//                "\"age_restriction\":\"\"" +
//                "\"studio\":\"\"," +
//                "\"trailer\":\"\"," +
//                "\"tag\":\"\"}]";
//        String age_restriction = "[{\"name\":\"Herr der Ringe\"," +
//                "\"original_name\":\"lord of the rings\"," +
//                "\"genre\":{\"action\",\"adventure\"}," +
//                "\"description\":\"Ein Ring sie zu knechten.\"," +
//                "\"length\":\"\"," +
//                "\"other\":\"\"," +
//                "\"actors\":\"\"," +
//                "\"directors\":\"\"," +
//                "\"year\":\"\"," +
//                "\"age_restriction\":\"fünfzehn\"" +
//                "\"studio\":\"\"," +
//                "\"trailer\":\"\"," +
//                "\"tag\":\"\"}]";
//
//        jsonList.add(year);
//        jsonList.add(age_restriction);
//
//        return jsonList.stream();
//    }
}
