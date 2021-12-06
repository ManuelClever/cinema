package de.manuelclever.cinema.seatblock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.manuelclever.cinema.database.query.PSQL.PSQLQueries;
import de.manuelclever.cinema.seatblock.seats.Seat;
import de.manuelclever.cinema.seatblock.seats.SeatType;
import de.manuelclever.cinema.util.LogGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

public class BlockOfSeats implements Iterable {
    private Map<Integer, Map<Integer, Seat>> seats;
    private int rows;
    private int columns;
    private String seatsJson;

    public BlockOfSeats(int rows, int columns) throws IndexOutOfBoundsException{
        if(rows > 0 && columns > 0) {
            this.seats = new HashMap<>();
            this.columns = columns;
            this.rows = rows;

            for (int row = 0; row < rows; row++) {
                seats.put(row, newRowOfStandardSeats(columns));
            }
        } else {
            throw new IndexOutOfBoundsException("Only natural numbers allowed.");
        }
    }

    private Map<Integer, Seat> newRowOfStandardSeats(int columns) {
        Map<Integer, Seat> newRowOfEmptySeats = new HashMap<>();
        for(int column = columns - 1; column >= 0; column--) {
            newRowOfEmptySeats.put(column, new Seat(SeatType.STANDARD));
        }

        return newRowOfEmptySeats;
    }

    public int[] getSize() {
        return new int[]{rows, columns};
    }

    public Seat getSeat(int row, int column) {
        if(isValidRow(row) && isValidColumn(column)) {
            Map<Integer, Seat> rowOfSeats = seats.get(row);
            return rowOfSeats.get(column);
        } return null;
    }

    private boolean isValidRow(int toCheck) {
        return toCheck >= 0 && toCheck <= rows;
    }

    private boolean isValidColumn(int toCheck) {
        return toCheck >= 0 && toCheck <= columns;
    }

    public boolean setAtPosition(int row, int column, SeatType type) {
       return setAtPosition(row, column, type, -1);
    }

    public boolean setAtPosition(int row, int column, SeatType type, int startOfSeat) {
       if(isValidRow(row) && isValidColumn(column)) {
           Map<Integer, Seat> rowOfSeats = seats.get(row);

            if(isPartOfSeat(startOfSeat)) {
                if (type.SIZE > 1) {
                    return setForEachPos(row, column, type);
                }
            }
            rowOfSeats.put(column, new Seat(type));
            return true;
        } return false;
    }

    private boolean isPartOfSeat(int colFirstPartOfSeat) {
        return colFirstPartOfSeat < 0;
    }

    private boolean setForEachPos(int row, int column, SeatType type) {
        if (isValidColumn(column + type.SIZE)) {
            for (int i = 0; i < type.SIZE; i++) {
                setAtPosition(row, column + i, type, column);
            }
            return true;
        } else return false;
    }

    public String getJson() {
        if(seatsJson == null) {
            return updateJson();
        }
        return seatsJson;
    }

    private String updateJson() {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode nodeOfSeats = createNodeOfSeats(mapper);

        ObjectNode nodeOfBlock = mapper.createObjectNode();
        nodeOfBlock.put(PSQLQueries.ROWS, rows);
        nodeOfBlock.put(PSQLQueries.COLUMNS, columns);
        nodeOfBlock.put(PSQLQueries.COLUMN_SEATS, nodeOfSeats);

        try {
            seatsJson = mapper.writeValueAsString(nodeOfBlock);
            return seatsJson;
        } catch(JsonProcessingException e) {
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
            return null;
        }
    }

    private ArrayNode createNodeOfSeats(ObjectMapper mapper) {
        SeatIterator iter = iterator();
        ArrayNode nodeOfSeats = mapper.createArrayNode();

        int lastSize = 1;
        while(iter.hasNext()) {
            ObjectNode seatNode = createSeatNode(mapper, iter, lastSize);
            nodeOfSeats.add(seatNode);
            lastSize = seatNode.get(PSQLQueries.SIZE).asInt();
        }
        return nodeOfSeats;
    }

    private ObjectNode createSeatNode(ObjectMapper mapper, SeatIterator iter, int lastSize) {
        Seat seat = iter.next();
        int row = iter.getCurrentRow();
        int column = iter.getCurrentColumn();
        char type = seat.getTypeChar();
        int size = seat.getSize();

        if(lastSize > 1) {
            size = --lastSize;
        }

        ObjectNode seatNode = mapper.createObjectNode();
        seatNode.put(PSQLQueries.ROW, row);
        seatNode.put(PSQLQueries.COLUMN, column);
        seatNode.put(PSQLQueries.TYPE, String.valueOf(type));
        seatNode.put(PSQLQueries.SIZE, size);
        return seatNode;
    }

    public void changeSize(int newRowSize, int newColumnSize) {
        addOrRemoveRows(newRowSize, newColumnSize);
        rows = newRowSize;
        addOrRemoveColumns(newColumnSize);
        columns = newColumnSize;

        updateJson();
    }

    private void addOrRemoveRows(int newRows, int newColumns) {
        int differenceRows = newRows - rows;

        if(differenceRows > 0) {
            addRows(differenceRows, newColumns);
        } else {
            removeRows(differenceRows);
        }
    }

    private void addRows(int differenceRows, int newColumns) {
        for(int row = rows; row < rows + differenceRows; row++) {
            seats.put(row, newRowOfStandardSeats(newColumns));
        }
    }

    private void removeRows(int differenceRows) {
        for(int row = rows-1; row >= rows + differenceRows; row--) {
            seats.remove(row);
        }
    }

    private void addOrRemoveColumns(int newColumns) {
        int differenceColumns = newColumns - columns;

        if(differenceColumns > 0) {
            addColumns(differenceColumns);
        } else {
            removeColumns(differenceColumns);
        }
    }

    private void addColumns(int differenceColumns) {
        for(int row = 0; row < rows; row++) {

            for(int column = columns; column < columns + differenceColumns; column++) {
                Seat seat = new Seat(SeatType.STANDARD);
                seats.get(row).put(column, seat);
            }
        }
    }

    private void removeColumns(int differenceColumns) {
        for(int row = 0; row < rows; row++) {

            for(int column = columns-1; column >= columns + differenceColumns; column--) {
                seats.get(row).remove(column);
            }
        }
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public SeatIterator iterator() {
        return new SeatIterator(this);
    }

    @Override
    public void forEach(Consumer action) {
        Iterator<Seat> iter = new SeatIterator(this);

        if(action != null) {
            while (iter.hasNext()) {
                Seat seat = iter.next();
                action.accept(seat);
            }
        }
    }

    @Override
    public int hashCode() {
        return seats.hashCode() * 37;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()) {
            return obj.hashCode() == this.hashCode();
        } return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Map<Integer, Seat> columns : seats.values()) {

            for(Seat seat : columns.values()) {
                sb.append(seat.getTypeChar()).append(",");
            }
            sb.replace(sb.length()-1, sb.length(), "");
            sb.append("\n");
        }
        sb.replace(sb.length()-1, sb.length(), "");
        return sb.toString();
    }
}
