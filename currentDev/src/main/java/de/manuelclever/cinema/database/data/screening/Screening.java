package de.manuelclever.cinema.database.data.screening;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Screening {
    private int screeningID;

    private int theaterID;
    private int movieID;
    private LocalDateTime timestamp;

    private final int SEC_TO_MILLI = 1000;

    public Screening() {
    }

    public Screening(int theaterID, int movieID, Long epochSeconds) {
        this.theaterID = theaterID;
        this.movieID = movieID;
        this.timestamp = Instant.ofEpochMilli(epochSeconds).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public Screening(int theaterID, int movieID, String timestamp) {
        this.theaterID = theaterID;
        this.movieID = movieID;
        this.timestamp = LocalDateTime.parse(timestamp);
    }

    public int getId() {
        return screeningID;
    }

    public void setId(int screeningID) {
        this.screeningID = screeningID;
    }

    public int getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(int theaterID) {
        this.theaterID = theaterID;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public LocalDateTime getLocalDateTime() {
        return timestamp;
    }

    public String getLocalDateTimeTrimmedString() {
        return timestamp.toString().replaceAll("0{2}$","");
    }

    public Long getEpochSeconds() {
        return timestamp.toEpochSecond(ZoneOffset.UTC);
    }

    public void setTimeEpochSeconds(Long epochSeconds) {
        this.timestamp = Instant.ofEpochMilli(epochSeconds).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public boolean setTimeString(String timeString) {
        try {

            this.timestamp = LocalDateTime.parse(timeString);
            return true;
        } catch (IllegalArgumentException e) {
            return  false;
        }
    }

    public boolean isValid() {
        return movieID != 0 &&
                timestamp != null &&
                theaterID != 0;
    }

    @Override
    public int hashCode() {
        return (movieID + (timestamp == null ? 0 : timestamp.hashCode()) + theaterID) * 37;
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
        return movieID + ", " + timestamp + ", " + theaterID;
    }
}
