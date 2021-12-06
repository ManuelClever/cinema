package de.manuelclever.cinema.database.datasource;

import de.manuelclever.cinema.database.datasource.util.CreateUrl;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DSCreator {
    private static BasicDataSource dataSourceCinemaDB;
    private static BasicDataSource testDataSourceCinemaDB;
    private static Properties properties;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private static final String DATABASE_PATH = "/data/cinemadb";
    // before running, create directory with permissions for user postgres
    private static final String DATABASE_SETTINGS_PATH = FileSystems
            .getDefault()
            .getPath("src", "main", "resources", "database")
            .toAbsolutePath()
            .toString();
    private static final String TABLESPACE = "cinema";

    public static DataSource getDataSource() {
        if(dataSourceCinemaDB == null) {

            try {
                dataSourceCinemaDB = new BasicDataSource();
                testDataSourceCinemaDB = new BasicDataSource();
                properties = new Properties();
                InputStream inputStream = new FileInputStream(
                        DATABASE_SETTINGS_PATH + FileSystems.getDefault().getSeparator() + "database.properties");
                properties.load(inputStream);

                BasicDataSource dataSourcePostgres = createDataSource("source_");
                createUserIfNotExist(dataSourcePostgres);
                createTableSpaceIfNotExist(dataSourcePostgres);

                createDatabaseIfNotExist(dataSourcePostgres);
                createTestDatabaseIfNotExist(dataSourcePostgres);

                setDataSource(dataSourceCinemaDB);
                setDataSource(testDataSourceCinemaDB, "test_");

                createSchemaIfNotExist(testDataSourceCinemaDB);
                createSchemaIfNotExist(dataSourceCinemaDB);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return dataSourceCinemaDB;
    }

    private static void printDatasource(BasicDataSource basicDS) {
        System.out.println("user=" + basicDS.getUsername() + ", passwort=" + basicDS.getPassword() + ", url=" + basicDS.getUrl() );
    }

    public static DataSource getTestDataSource() {
        if(dataSourceCinemaDB == null) {
            getDataSource();
        }
        return testDataSourceCinemaDB;
    }

    private static BasicDataSource createDataSource(String type) throws IOException {
        BasicDataSource dataSourcePostgres = new BasicDataSource();
        setDataSource(dataSourcePostgres, type);
        return dataSourcePostgres;
    }

    private static void setDataSource(BasicDataSource basicDS) throws IOException{
        setDSConnection(basicDS, "");
        setDSPool(basicDS);
    }

    private static void setDataSource(BasicDataSource basicDS, String type) throws IOException{
        setDSConnection(basicDS, type);
        setDSPool(basicDS);
    }

    private static void setDSConnection(BasicDataSource basicDS, String type) throws IOException {
        basicDS.setDriverClassName(properties.getProperty("driverClass"));
        basicDS.setUsername(properties.getProperty(type + "user"));
        basicDS.setPassword(properties.getProperty(type + "password"));
        basicDS.setUrl(getUrl(
                basicDS.getDriverClassName(),
                properties.getProperty("server"),
                properties.getProperty("port"),
                properties.getProperty(type + "database")));

        String tempProperty = properties.getProperty(type + "defaultAutoCommit");
        if (tempProperty != null) {
            basicDS.setDefaultAutoCommit(Boolean.parseBoolean(tempProperty));
        }
    }

    private static String getUrl(String driverClassName, String serverName, String port, String database) throws IOException {
        String url = CreateUrl.get(driverClassName, serverName, port, database);
        if (url == null) {
            throw new IOException("Incorrect Database Url");
        }
        return url;
    }

    private static BasicDataSource setDSPool(BasicDataSource basicDS) {
        String tempProperty = properties.getProperty("initialSize");
        if (tempProperty != null) {
            try {
                basicDS.setInitialSize(Integer.parseInt(tempProperty));
            } catch (NumberFormatException ignore) {
            }
        }

        tempProperty = properties.getProperty("maxTotal");
        if (tempProperty != null) {
            try {
                basicDS.setInitialSize(Integer.parseInt(tempProperty));
            } catch (NumberFormatException ignore) {
            }
        }

        tempProperty = properties.getProperty("maxIdle");
        if (tempProperty != null) {
            try {
                basicDS.setMaxIdle(Integer.parseInt(tempProperty));
            } catch (NumberFormatException ignore) {
            }
        }

        tempProperty = properties.getProperty("minIdle");
        if (tempProperty != null) {
            try {
                basicDS.setMinIdle(Integer.parseInt(tempProperty));
            } catch (NumberFormatException ignore) {
            }
        }

        tempProperty = properties.getProperty("maxWaitMillis");
        if (tempProperty != null) {
            try {
                basicDS.setMaxWaitMillis(Long.parseLong(tempProperty));
            } catch (NumberFormatException ignore) {
            }
        }
        return basicDS;
    }

    private static void createUserIfNotExist(BasicDataSource basicDS) {
        printDatasource(basicDS);
        String statement =
                "CREATE USER " + properties.getProperty("user") +
                " WITH ENCRYPTED PASSWORD '" + properties.getProperty("password") + "';";
        execute(basicDS, statement);
    }

    private static void execute(BasicDataSource basicDS, String statement) {
        try(Connection con = basicDS.getConnection()) {
            Statement st = con.createStatement();
            System.out.println(statement);
            System.out.println("---");
            st.execute(statement);
            con.commit();
            st.close();
        } catch (SQLException e) {
            System.out.println(ANSI_YELLOW + e.getMessage() + ANSI_RESET);
        }
    }

    private static void executeAuto(BasicDataSource basicDS, String statement) {
        try(Connection con = basicDS.getConnection()) {
            con.setAutoCommit(true);
            Statement st = con.createStatement();
            System.out.println(statement);
            System.out.println("---");
            st.execute(statement);
            st.close();
        } catch (SQLException e) {
            System.out.println(ANSI_YELLOW + e.getMessage() + ANSI_RESET);
        }
    }

    public static void createTableSpaceIfNotExist(BasicDataSource basicDS) {
        printDatasource(basicDS);
        String statement = "CREATE TABLESPACE " + TABLESPACE + " LOCATION '" + DATABASE_PATH + "';";
        executeAuto(basicDS, statement);
    }

    public static void createSchemaIfNotExist(BasicDataSource basicDS) {
        printDatasource(basicDS);
        String statement =
                "CREATE SCHEMA " + properties.getProperty("schema") +
                        " AUTHORIZATION " + properties.getProperty("user") + "\n" +
                        "CREATE TABLE customer (\n" +
                            PSQLQueries.CUSTOMER_ID +  " serial PRIMARY KEY,\n" +
                            PSQLQueries.CUSTOMER_LAST_NAME + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_FIRST_NAME + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_BIRTHDAY + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_EMAIL + " text,\n" +
                            PSQLQueries.CUSTOMER_DATE_OF_ENTRY + " date NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_STREET + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_HOUSE_NUM + " smallint NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_HOUSE_EXTRA + " text, \n" +
                            PSQLQueries.CUSTOMER_POSTAL + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_CITY + " text NOT NULL,\n" +
                            PSQLQueries.CUSTOMER_COUNTRY + " text NOT NULL )\n" +
                        "CREATE TABLE bonus_card (\n" +
                            PSQLQueries.BONUS_CARD_ID + " int REFERENCES " + PSQLQueries.TABLE_CUSTOMER + ",\n" +
                            PSQLQueries.BONUS_CARD_BALANCE + " float4,\n" +
                            PSQLQueries.BONUS_CARD_POINTS + " int )\n" +
                        "CREATE TABLE theater (\n" +
                            PSQLQueries.THEATER_ID + " serial PRIMARY KEY,\n" +
                            PSQLQueries.THEATER_NAME + " text NOT NULL,\n" +
                            PSQLQueries.THEATER_BLOCK + " json NOT NULL,\n" +
                            PSQLQueries.THEATER_EXTRAS + " text )\n" +
                        "CREATE TABLE movie (\n" +
                            PSQLQueries.MOVIE_ID + " serial PRIMARY KEY,\n" +
                            PSQLQueries.MOVIE_NAME + " text NOT NULL,\n" +
                            PSQLQueries.MOVIE_ORIGINAL_NAME + " text NOT NULL,\n" +
                            PSQLQueries.MOVIE_GENRE + " text,\n" +
                            PSQLQueries.MOVIE_DESCRIPTION + " text,\n" +
                            PSQLQueries.MOVIE_LENGTH + " smallint,\n" +
                            PSQLQueries.MOVIE_OTHER + " text,\n" +
                            PSQLQueries.MOVIE_ACTORS + " text,\n" +
                            PSQLQueries.MOVIE_DIRECTORS + " text,\n" +
                            PSQLQueries.MOVIE_COUNTRY + " text,\n" +
                            PSQLQueries.MOVIE_YEAR + " int,\n" +
                            PSQLQueries.MOVIE_AGE_RESTR + " smallint,\n" +
                            PSQLQueries.MOVIE_STUDIO + " text,\n" +
                            PSQLQueries.MOVIE_TRAILER + " text,\n" +
                            PSQLQueries.MOVIE_TAGS + " text)\n" +
                        "CREATE TABLE screening (\n" +
                            PSQLQueries.SCREENING_ID + " serial PRIMARY KEY,\n" +
                            PSQLQueries.MOVIE_ID + " int REFERENCES " + PSQLQueries.TABLE_MOVIE + ",\n" +
                            PSQLQueries.SCREENING_TIME + " timestamp NOT NULL,\n" +
                            PSQLQueries.THEATER_ID + " int REFERENCES " + PSQLQueries.TABLE_THEATER + ")\n" +
                        "CREATE TABLE booking (\n" +
                            PSQLQueries.BOOKING_ID + " serial PRIMARY KEY,\n" +
                            PSQLQueries.CUSTOMER_ID + " int REFERENCES " + PSQLQueries.TABLE_CUSTOMER + ",\n" +
                            PSQLQueries.SCREENING_ID + " int REFERENCES " + PSQLQueries.TABLE_SCREENING + ",\n" +
                            PSQLQueries.BOOKING_BILL + " json NOT NULL);";
        execute(basicDS, statement);
    }

    private static void createDatabaseIfNotExist(BasicDataSource basicDS) {
        printDatasource(basicDS);
        String statement =
                "CREATE DATABASE " + properties.getProperty("database") +
                    " OWNER " + properties.getProperty("user") +
                    " TABLESPACE " + TABLESPACE + ";";
        executeAuto(basicDS, statement);
    }

    public static void createTestDatabaseIfNotExist(BasicDataSource basicDS) {
        printDatasource(basicDS);
        String statement =
                "CREATE DATABASE " + properties.getProperty("test_database") +
                        " OWNER " + properties.getProperty("user") +
                        " TABLESPACE " + TABLESPACE + ";";
        executeAuto(basicDS, statement);
    }
}
