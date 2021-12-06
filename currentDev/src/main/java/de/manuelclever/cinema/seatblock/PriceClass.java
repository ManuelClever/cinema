package de.manuelclever.cinema.seatblock;

import de.manuelclever.cinema.util.LogGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Properties;
import java.util.logging.Level;

public class PriceClass {
    private double basePrice;
    private double multiplikatorA;
    private double multiplikatorB;
    private double multiplikatorC;
    private double multiplikatorD;
    private double multiplikatorE;
    private double multiplikatorF;
    private double multiplikatorG;

    private Properties properties;
    private final String PROPERTIES_PATH = FileSystems
            .getDefault()
            .getPath("src", "main", "resources", "database", "cinema.properties")
            .toAbsolutePath()
            .toString();

    public PriceClass() {
        try {
            properties = new Properties();
            InputStream inputStream = new FileInputStream(PROPERTIES_PATH);
            properties.load(inputStream);

            basePrice = Double.parseDouble(properties.getProperty("basePrice"));
            multiplikatorA = Double.parseDouble(properties.getProperty("multiplikatorA"));
            multiplikatorB = Double.parseDouble(properties.getProperty("multiplikatorB"));
            multiplikatorC = Double.parseDouble(properties.getProperty("multiplikatorC"));
            multiplikatorD = Double.parseDouble(properties.getProperty("multiplikatorD"));
            multiplikatorE = Double.parseDouble(properties.getProperty("multiplikatorE"));
            multiplikatorF = Double.parseDouble(properties.getProperty("multiplikatorF"));
            multiplikatorG = Double.parseDouble(properties.getProperty("multiplikatorG"));
        } catch (IOException |NumberFormatException e){
            LogGenerator.log(Level.WARNING, getClass(), e.getMessage());
        }
    }

    public PriceClass(Double price) {
        this();
        basePrice = price;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getMultiplikatorA() {
        return multiplikatorA;
    }

    public void setMultiplikatorA(double multiplikatorA) {
        this.multiplikatorA = multiplikatorA;
    }

    public double getMultiplikatorB() {
        return multiplikatorB;
    }

    public void setMultiplikatorB(double multiplikatorB) {
        this.multiplikatorB = multiplikatorB;
    }

    public double getMultiplikatorC() {
        return multiplikatorC;
    }

    public void setMultiplikatorC(double multiplikatorC) {
        this.multiplikatorC = multiplikatorC;
    }

    public double getMultiplikatorD() {
        return multiplikatorD;
    }

    public void setMultiplikatorD(double multiplikatorD) {
        this.multiplikatorD = multiplikatorD;
    }

    public double getMultiplikatorE() {
        return multiplikatorE;
    }

    public void setMultiplikatorE(double multiplikatorE) {
        this.multiplikatorE = multiplikatorE;
    }

    public double getMultiplikatorF() {
        return multiplikatorF;
    }

    public void setMultiplikatorF(double multiplikatorF) {
        this.multiplikatorF = multiplikatorF;
    }

    public double getMultiplikatorG() {
        return multiplikatorG;
    }

    public void setMultiplikatorG(double multiplikatorG) {
        this.multiplikatorG = multiplikatorG;
    }
}
