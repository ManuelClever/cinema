package de.manuelclever.cinema.database.data.theater;

import de.manuelclever.cinema.seatblock.BlockOfSeats;

import java.util.Arrays;
import java.util.List;

public abstract class Theater {
    private int id;
    private String name;
    private BlockOfSeats blockOfSeats;
    private List<String> extras;

    public Theater() {
    }

    public Theater(String name, BlockOfSeats blockOfSeats, String extras) {
        this.name = name;
        this.blockOfSeats = blockOfSeats;
        setExtras(extras);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BlockOfSeats getSeats() {
        return blockOfSeats;
    }

    public String getSeatsJson() {
        if(blockOfSeats != null) {
            return blockOfSeats.getJson();
        } return null;
    }

    public void setSeats(BlockOfSeats blockOfSeats) {
        this.blockOfSeats = blockOfSeats;
    }

    public int[] size() {
        return blockOfSeats.getSize();
    }

    public void changeSize(int rows, int columns) {
        blockOfSeats.changeSize(rows, columns);
    }

    public void setExtras(String extras) {
        String[] extrasArr = extras.split(",");
        this.extras = Arrays.asList(extrasArr);
    }

    public void addExtra(String extra) {
        extras.add(extra);
    }

    public void removeExtra(String extra) {
        extras.remove(extra);
    }

    public List<String> getExtras() {
        return extras;
    }

    public boolean isValid() {
        return name != null && !name.equals("") &&
                blockOfSeats != null;
    }

    @Override
    public int hashCode() {
        int multiplier = 37;
        int hashCode = (name.hashCode() + extras.hashCode()) * multiplier;

        if(blockOfSeats != null) {
            return hashCode + (blockOfSeats.hashCode() * multiplier);
        } return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return obj.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + name + ", " + blockOfSeats + ", " + extras + "]";
    }
}
