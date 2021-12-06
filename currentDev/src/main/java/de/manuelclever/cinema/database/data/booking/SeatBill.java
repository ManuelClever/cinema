package de.manuelclever.cinema.database.data.booking;

public class SeatBill {
    private int row;
    private int column;
    private double price;
    private double multiplier;
    private double discount;

    public SeatBill() {
    }

    public SeatBill(int row, int column, double price, double multiplier, double discount) {
        this.row = row;
        this.column = column;
        this.price = price;
        this.multiplier = multiplier;
        this.discount = discount;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public int hashCode() {
        return (row + column + (int) price + (int) multiplier + (int) discount) * 37;
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
        return "[" + row + ", " + column + ", " + price + ", " + multiplier + ", " + discount + "]";
    }
}
