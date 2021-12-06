package de.manuelclever.cinema.database.data.movie;

public enum Genre {
    ACTION,
    ADVENTURE,
    BIOGRAPHY,
    COMEDY,
    DOCUMENTARY,
    DRAMA,
    HORROR,
    ROMANCE;

    public static Genre get(String s) {
        switch(s.toLowerCase()) {
            case "action":
                return ACTION;
            case "adventure":
                return ADVENTURE;
            case "biography":
                return BIOGRAPHY;
            case "comedy":
                return COMEDY;
            case "documentary":
                return DOCUMENTARY;
            case "drama":
                return DRAMA;
            case "horror":
                return HORROR;
            case "romance":
                return ROMANCE;
            default:
                return null;
        }
    }
}
