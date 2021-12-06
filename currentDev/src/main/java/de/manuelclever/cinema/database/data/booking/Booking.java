package de.manuelclever.cinema.database.data.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.manuelclever.cinema.database.query.SQLQueries;
import de.manuelclever.cinema.util.LogGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Booking {
    private int bookingID;
    private int screeningID;
    private int customerID;
    private List<SeatBill> bill;
    private double discount;

    public Booking() {
        this.bill = new ArrayList<>();
    }

    public Booking(int screeningID, int customerID, List<SeatBill> bill) {
        this.screeningID = screeningID;
        this.customerID = customerID;
        this.bill = bill;
    }

    public int getId() {
        return bookingID;
    }

    public void setBookingId(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getScreeningID() {
        return screeningID;
    }

    public void setScreeningID(int screeningID) {
        this.screeningID = screeningID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public List<SeatBill> getBill() {
        return bill;
    }

    public String getBillJson() {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode nodeOfSeatBills = mapper.createArrayNode();

        for(SeatBill seatBill : bill)  {
            ObjectNode seatBillNode = createSeatBillNode(mapper, seatBill);
            nodeOfSeatBills.add(seatBillNode);
        }

        try {
            String billJson = mapper.writeValueAsString(nodeOfSeatBills);
            return billJson;
        } catch(JsonProcessingException e) {
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
        return null;
    }

    private ObjectNode createSeatBillNode(ObjectMapper mapper, SeatBill seatBill) {
        int row = seatBill.getRow();
        int column = seatBill.getColumn();
        double price = seatBill.getPrice();
        double multiplier = seatBill.getMultiplier();
        double discount = seatBill.getDiscount();

        ObjectNode seatBillNode = mapper.createObjectNode();
        seatBillNode.put(SQLQueries.ROW, row);
        seatBillNode.put(SQLQueries.COLUMN, column);
        seatBillNode.put(SQLQueries.PRICE, price);
        seatBillNode.put(SQLQueries.MULTIPLIER, multiplier);
        seatBillNode.put(SQLQueries.DISCOUNT, discount);
        return  seatBillNode;
    }

    public void addBill(SeatBill... bills) {
        Arrays.asList(bills).forEach(bill -> {
            if(bill != null) {
                this.bill.add(bill);
            }
        });
    }

    public void setBill(List<SeatBill> bill) {
        this.bill = bill;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public boolean isValid() {
        return customerID != 0 &&
                screeningID != 0 &&
                bill != null;
    }

    @Override
    public int hashCode() {
        return (bookingID + screeningID + customerID + bill.hashCode() + (int) discount) * 37;
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
        return "[" + bookingID + ", " + screeningID + ", " + customerID + ", " + bill + ", " + discount + "]";
    }
}
