package de.manuelclever.cinema.database.datasource.util;

import java.util.ArrayList;

public class CustomArrayList<T> extends ArrayList<T> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        this.forEach(entry -> sb.append(entry.toString()).append(","));
        if(sb.length() > 0) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }

        return sb.toString();
    }
}
