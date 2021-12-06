package de.manuelclever.cinema.database.data.movie;

import de.manuelclever.cinema.database.datasource.util.CustomArrayList;

import java.util.Arrays;
import java.util.List;

public abstract class Movie {
    private int id;
    private String name;
    private String originalName;
    private List<Genre> genre;
    private String description;
    private int length;
    private List<String> other;

    private List<String> actors;
    private List<String> directors;
    private String country;
    private int year;
    private int ageRestriction;
    private String studio;

    private List<String> trailer;
    private List<String> tags;

    public Movie() {
        this("", "");
    }

    public Movie(String name, String originalName) {
        this.name = name;
        this.originalName = originalName;
        this.description = "";
        this.country = "";
        this.studio = "";

        this.genre = new CustomArrayList<>();
        this.other = new CustomArrayList<>();
        this.actors = new CustomArrayList<>();
        this.directors = new CustomArrayList<>();
        this.trailer = new CustomArrayList<>();
        this.tags = new CustomArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setName(String name, String originalName) {
        this.name = name;
        this.originalName = originalName;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void addGenre(Genre... genres) {
        Arrays.asList(genres).forEach(genre -> {
            if(genre != null) {
                this.genre.add(genre);
            }
        });
    }

    public void removeGenre(Genre genre) {
        this.genre.remove(genre);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<String> getOther() {
        return other;
    }

    public void addOther(String... other) {
        Arrays.asList(other).forEach(string -> {
            if(string != "") {
                this.other.add(string);
            }
        });
    }

    public List<String> getActors() {
        return actors;
    }

    public void addActor(String... actors) {
        Arrays.asList(actors).forEach(actor -> {
            if(actor != "") {
                this.actors.add(actor);
            }
        });
    }
    
    public void removeActor(String actor) {
        this.actors.remove(actor);
    }

    public List<String> getDirectors() {
        return directors;
    }

    public void addDirector(String... directors) {
        Arrays.asList(directors).forEach(director -> {
            if(director != "") {
                this.directors.add(director);
            }
        });
    }
    
    public void removeDirector(String director) {
        this.directors.remove(director);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(int ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public List<String> getTrailer() {
        return trailer;
    }

    public void addTrailer(String... trailer) {
        Arrays.asList(trailer).forEach(link -> {
            if(link != "") {
                this.trailer.add(link);
            }
        });
    }

    public void removeTrailer(String trailer) {
        this.trailer.remove(trailer);
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String... tags) {
        Arrays.asList(tags).forEach(tag -> {
            if(!tag.equals("")) {
                this.tags.add(tag);
            }
        });
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public boolean isValid() {
        return name != null && !name.equals("") &&
                originalName != null && !originalName.equals("");
    }

    @Override
    public int hashCode() {
        return (name.toLowerCase().hashCode() + originalName.toLowerCase().hashCode() + genre.hashCode() +
                description.hashCode() + length + other.hashCode() + actors.hashCode() + directors.hashCode() +
                year + ageRestriction + studio.hashCode() + trailer.hashCode() + tags.hashCode()) * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()) {
            return obj.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + name + ", " + originalName + ", " + genre + ", " + description + ", " + length + ", " + other +
                ", " + actors + ", " + directors + ", " + year + ", " + ageRestriction + ", " + studio + ", " +
                trailer + ", " + tags + "]";
    }
}
