package de.manuelclever.database.parser;

import de.manuelclever.cinema.database.data.customer.Address;
import de.manuelclever.cinema.database.data.customer.Customer;
import de.manuelclever.cinema.database.parser.MyJsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ParseCustomerTest {

    @Test
    public void withMandatoryInput() {
        Customer customer = new Customer("Mustermann", "Max", "1997-04-28", "max@email.com", "2021-01-01",
                new Address("Musterweg", 25, "65307", "Musterstadt", new Locale("", "DE")));
        String json =  "[{\"customer_id\":\"\"," +
                "\"last_name\":\"Mustermann\"," +
                "\"first_name\":\"Max\"," +
                "\"birthday\":\"1997-04-28\"," +
                "\"email\":\"max@email.com\"," +
                "\"date_of_entry\":\"2021-01-01\"," +
                "\"street\":\"Musterweg\"," +
                "\"number\":25," +
                "\"extra\":\"\"," +
                "\"postal\":\"65307\"," +
                "\"city\":\"Musterstadt\"," +
                "\"country\":\"DE\"}]";

        Assertions.assertEquals(customer, MyJsonParser.parseCustomer(json));
    }

    @Test
    public void withoutMandatoryInputOfTypeString() {
        Customer customer = new Customer();
        //set house_number to 1, because if house_number is left empty, or 0
        //a NumberFormatException is thrown and null is being returned
        Address address = new Address();
        address.setNumber(1);
        customer.setAddress(address);
        String json = "[{\"customer_id\":\"\"," +
                "\"last_name\":\"\"," +
                "\"first_name\":\"\"," +
                "\"birthday\":\"" + LocalDate.parse("0000-01-01") + "\"," +
                "\"email\":\"\"," +
                "\"date_of_entry\":\"" + LocalDate.parse("0000-01-01") + "\"," +
                "\"street\":\"\"," +
                "\"number\":1," +
                "\"extra\":\"\"," +
                "\"postal\":\"\"," +
                "\"city\":\"\"," +
                "\"country\":\"\"}]";

        Assertions.assertEquals(customer, MyJsonParser.parseCustomer(json));
    }

    @ParameterizedTest
    @MethodSource()
    public void wrongInputType(String json) {
        Assertions.assertNull(MyJsonParser.parseCustomer(json));
    }

    private static Stream<String> wrongInputType() {
        List<String> jsonList = new ArrayList<>();

        String birthday = "[{\"customer_id\":\"\"," +
                "\"last_name\":\"Mustermann\"," +
                "\"first_name\":\"Max\"," +
                "\"birthday\":\"08-04-1997\"," +
                "\"email\":\"max@email.com\"," +
                "\"date_of_entry\":\"2021-01-01\"," +
                "\"street\":\"Musterweg\"," +
                "\"number\":25," +
                "\"extra\":\"\"," +
                "\"postal\":\"65307\"," + 
                "\"city\":\"Musterstadt\"," +
                "\"country\":\"DE\"}]";
        String date_of_entry = "[{\"customer_id\":\"\"," +
                "\"last_name\":\"Mustermann\"," +
                "\"first_name\":\"Max\"," +
                "\"birthday\":\"1997-04-08\"," +
                "\"email\":\"max@email.com\"," +
                "\"date_of_entry\":\"2021.01.01\"," +
                "\"street\":\"Musterweg\"," +
                "\"number\":25," +
                "\"extra\":\"\"," +
                "\"postal\":\"65307\"," +
                "\"city\":\"Musterstadt\"," +
                "\"country\":\"DE\"}]";
        String postal = "[{\"customer_id\":\"\"," +
                "\"last_name\":\"Mustermann\"," +
                "\"first_name\":\"Max\"," +
                "\"birthday\":\"1997-04-08\"," +
                "\"email\":\"max@email.com\"," +
                "\"date_of_entry\":\"2021-01-01\"," +
                "\"street\":\"Musterweg\"," +
                "\"number\":25," +
                "\"extra\":\"\"," +
                "\"postal\":\"65b07\"," +
                "\"city\":\"Musterstadt\"," +
                "\"country\":\"DE\"}]";

        jsonList.add(birthday);
        jsonList.add(date_of_entry);
        jsonList.add(postal);

        return jsonList.stream();
    }
}
