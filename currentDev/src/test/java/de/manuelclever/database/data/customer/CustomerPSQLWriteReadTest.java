package de.manuelclever.database.data.customer;

import de.manuelclever.cinema.database.data.customer.Address;
import de.manuelclever.cinema.database.data.customer.Customer;
import de.manuelclever.cinema.database.data.customer.PSQLCustomerReader;
import de.manuelclever.cinema.database.data.customer.PSQLCustomerWriter;
import de.manuelclever.cinema.database.datasource.DSCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

public class CustomerPSQLWriteReadTest {
    private static PSQLCustomerWriter customerWriter;
    private static PSQLCustomerReader customerReader;

    private static Map<String, Integer> validCustomerIds;

    @BeforeAll
    private static void writeValidCustomers() {
        initialize();
        validCustomers().forEach(customer -> {
            int id = customerWriter.addCustomer(customer);
            validCustomerIds.put(customer.getBirthday().toString(), id);
        });
    }

    private static void initialize() {
        customerWriter = new PSQLCustomerWriter(DSCreator.getTestDataSource());
        customerReader = new PSQLCustomerReader(DSCreator.getTestDataSource());

        validCustomerIds = new HashMap<>();
    }

    @AfterAll
    private static void removeAllCustomers() {
        validCustomerIds.forEach((birthday,id) -> customerWriter.removeCustomer(id));
    }

    @ParameterizedTest
    @MethodSource("validCustomers")
    public void readReturnedJson(Customer customer) {
        int id = validCustomerIds.get(customer.getBirthday().toString());

        String returnedCustomer = customerReader.getCustomer(id);
        String expectedJson = "[{\"customer_id\":" + id + "," +
                "\"last_name\":\"" + customer.getLastName() + "\"," +
                "\"first_name\":\"" + customer.getFirstName() + "\"," +
                "\"birthday\":\"" + customer.getBirthday() + "\"," +
                "\"email\":\"" + customer.getEmail() + "\"," +
                "\"date_of_entry\":\"" + customer.getDateOfEntry() + "\"," +
                "\"street\":\"" + customer.getStreet() + "\"," +
                "\"number\":" + customer.getHouseNum() + "," +
                "\"extra\":\"" + customer.getHouseExtra() + "\"," +
                "\"postal\":\"" + customer.getPostal() + "\"," +
                "\"city\":\"" + customer.getCity() + "\"," +
                "\"country\":\"" + customer.getCountry() + "\"}]";

        Assertions.assertEquals(expectedJson, returnedCustomer);
    }

    //birthdays have to be different!
    public static Stream<Customer> validCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Mustermann", "Max", "1999-12-01",
                "max@mail.de", "2010-01-01",
                new Address("Straße", 15, "65432", "Stadt", new Locale("", "DE"))));
        customers.add(new Customer("Thomas", "class", "1959-02-01",
                "class@mail.de", "2014-01-01",
                new Address("Straße", 5824, "65432", "Stadt", new Locale("", "DE"))));
        customers.add(new Customer("true", "Cloud", "1957-04-15",
                "cloud@mail.de", "2019-07-13",
                new Address("Straße", 157, "65432", "Stadt", new Locale("", "CH"))));
        customers.add(new Customer("Cloud", "false", "1957-04-13",
                "cloud@mail.de", "2019-07-13",
                new Address("Straße", 157, "65432", "Stadt", new Locale("", "CH"))));
        return customers.stream();
    }

    @ParameterizedTest
    @MethodSource("invalidCustomers")
    public void writeInvalidCustomer(Customer customer) {
        int id = customerWriter.addCustomer(customer);

        String returnedCustomer = customerReader.getCustomer(id);
        Assertions.assertEquals(null, returnedCustomer);
    }

    public static Stream<Customer> invalidCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Mustermann", null, "1999-12-01",
                "max@mail.de", "2010-01-01",
                new Address("Straße", 15, "65432", "Stadt", new Locale("", "DE"))));
        customers.add(new Customer("Mustermann", "", "1999-12-01",
                "max@mail.de", "2010-01-01",
                new Address("Straße", 15, "65432", "Stadt", new Locale("", "DE"))));
        customers.add(new Customer("Thomas", "class", "1959-02-01",
                "class@mail.de", "2014-01-01",
                new Address("Straße", 452527828, "65432", "Stadt", new Locale("", "DE"))));
        return customers.stream();
    }

    @ParameterizedTest
    @MethodSource("validCustomers")
    public void removeValidCustomer(Customer customer) {
        int id = customerWriter.addCustomer(customer);
        customerWriter.removeCustomer(id);

        String returnedCustomer = customerReader.getCustomer(id);
        Assertions.assertNull(returnedCustomer);
    }

//    @ParameterizedTest
//    @ValueSource(ints = {1, -33, 0, 285})
//    public void removeInvalidCustomer(int id) {
//        PSQLCustomerWriter writer = new PSQLCustomerWriter(DSCreator.getTestDataSource());
//        boolean removed = writer.removeCustomer(id);
//
//        Assertions.assertFalse(removed);
//    }
}
