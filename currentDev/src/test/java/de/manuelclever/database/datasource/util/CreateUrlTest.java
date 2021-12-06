package de.manuelclever.database.datasource.util;

import de.manuelclever.cinema.database.datasource.util.CreateUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateUrlTest {

    @Test
    public void createUrlPostgreSQL() {
        String postgreSQL = "org.postgresql.Driver";
        String serverName = "host";
        String portNumber = "port";
        String databaseName = "database";

        String expected = "jdbc:postgresql://host:port/database";

        Assertions.assertEquals(expected, CreateUrl.get(postgreSQL, serverName, portNumber, databaseName));
    }
}
