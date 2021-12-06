package de.manuelclever.cinema.database.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.manuelclever.cinema.database.data.customer.Address;
import de.manuelclever.cinema.database.data.customer.Customer;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.util.LogGenerator;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

public class CustomerDeserializer extends StdDeserializer<List<Customer>> {

    public CustomerDeserializer() {
        this(null);
    }

    public CustomerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Customer> deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode customersNode = parser.getCodec().readTree(parser);
        List<Customer> customers = new ArrayList<>();

        for (JsonNode customerNode : customersNode) {
            try {
                Address address = createAddress(customerNode);
                if(address != null) {
                    Customer customer = createCustomer(customerNode, address);
                    customers.add(customer);
                } else return null;

            } catch (DateTimeParseException | IllegalArgumentException e) {
                LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
                return null;
            }
        }
        return customers;
    }

    private Address createAddress(JsonNode customerNode) {
        Address address = new Address();

        try {
            address.setStreet(customerNode.get(PSQLQueries.CUSTOMER_STREET).asText());
            address.setExtra(customerNode.get(PSQLQueries.CUSTOMER_HOUSE_EXTRA).asText());
            address.setCity(customerNode.get(PSQLQueries.CUSTOMER_CITY).asText());

            int house = customerNode.get(PSQLQueries.CUSTOMER_HOUSE_NUM).asInt();
            if(house == 0) {
                throw new DataFormatException("int expected");
            }
            address.setNumber(house);

            String postal = customerNode.get(PSQLQueries.CUSTOMER_POSTAL).asText();
            if(!postal.equals("")) {
                try {
                    Integer.parseInt(postal);
                    address.setPostal(postal);
                } catch (NumberFormatException e) {
                    return null;
                }
            }

            String country = customerNode.get(PSQLQueries.CUSTOMER_COUNTRY).asText();
            country = country.replaceAll("\\w*[_]", "");
            if(!country.equals("")) {
                if (isISOCountry(country)) {
                    address.setCountry(new Locale("", country));
                } else throw new IllegalArgumentException("ISO not valid");
            }
        } catch(IllegalArgumentException | DataFormatException e) {
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return address;
    }

    private Customer createCustomer(JsonNode customerNode, Address address) {
        Customer customer = new Customer();
        customer.setAddress(address);

        try {
            customer.setId(customerNode.get(PSQLQueries.CUSTOMER_ID).asInt());
            customer.setLastName(customerNode.get(PSQLQueries.CUSTOMER_LAST_NAME).asText());
            customer.setFirstName(customerNode.get(PSQLQueries.CUSTOMER_FIRST_NAME).asText());
            customer.setEmail(customerNode.get(PSQLQueries.CUSTOMER_EMAIL).asText());

            String date = customerNode.get(PSQLQueries.CUSTOMER_BIRTHDAY).asText();
            if(!date.equals("")) {
                LocalDate birthday = LocalDate.parse(date);
                customer.setBirthday(birthday);
            }

            date = customerNode.get(PSQLQueries.CUSTOMER_DATE_OF_ENTRY).asText();
            if(!date.equals("")) {
                Date dateOfEntry = Date.valueOf(date);
                customer.setDateOfEntry(dateOfEntry);
            }
        } catch (IllegalArgumentException | DateTimeParseException e) {
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
        return customer;
    }

    private boolean isISOCountry(String country){
        if(country.equals("DE") || country.equals("FR") ||country.equals("NL")) {
            return true;
        } else {
            String[] isoCountries = Locale.getISOCountries();

            for(String iso : isoCountries) {
                if(iso.equals(country)) {
                    return true;
                }
            }
        }
        return false;
    }
}

