package de.manuelclever.cinema.database.data.customer;

import java.util.Locale;

public class Address {
    private String street;
    private String extra;
    private int number;

    private String postal;
    private String city;
    private Locale country;

    public Address() {
        this.street = "";
        this.extra = "";
        this.postal = "";
        this.city = "";
        this.country = new Locale("");
    }

    public Address(String street, int number, String postal, String city, Locale country) {
        this.street = street;
        this.number = number;
        this.extra = "";
        this.postal = postal;
        this.city = city;
        this.country = country;
    }

    public Address(String street, int number, String extra, String postal, String city, Locale country) {
        this.street = street;
        this.number = number;
        this.extra = extra;
        this.postal = postal;
        this.city = city;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Locale getCountry() {
        return country;
    }

    public void setCountry(Locale country) {
        this.country = country;
    }

    public boolean isValid() {
        return street != null && !street.equals("") &&
                number != 0 &&
                postal != null && !postal.equals("") &&
                city != null && !city.equals("") &&
                country != null;
    }

    @Override
    public int hashCode() {
        return street.hashCode() + number + postal.hashCode() + city.hashCode() * 41;
    }

    @Override
    public String toString() {
        return "[" + street + " " + number + extra + ", " + postal + " " + city + ", " + country + "]";
    }
}
