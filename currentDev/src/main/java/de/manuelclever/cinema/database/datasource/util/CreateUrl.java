package de.manuelclever.cinema.database.datasource.util;

public class CreateUrl {
    private static final String POSTGRESQL = "org.postgresql.Driver";

    public static String get(String driverClassName, String serverName, String portNumber, String databaseName) {
        if(driverClassName.equals(POSTGRESQL)) {
            return "jdbc:postgresql://" + serverName + ":" + portNumber + "/" + databaseName;
        }
        return null;
    }
}
