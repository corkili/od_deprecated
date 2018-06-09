package org.kai.od.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class OpticalDevice implements SerializableData {

    private Long id;

    private String name;

    private String type;

    private TopCategory topCategory;

    private Category category;

    private Double price;

    private String characteristics;

    private String describe;

    private Map<Long, Manufactor> representativeManufactors;

    public OpticalDevice() {

    }

    public OpticalDevice(Long id, String name, String type, TopCategory topCategory, Category category,
                         Double price, String characteristics, String describe,
                         Map<Long, Manufactor> representativeManufactors) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.topCategory = topCategory;
        this.category = category;
        this.price = price;
        this.characteristics = characteristics;
        this.describe = describe;
        this.representativeManufactors = representativeManufactors;
    }

    @Override
    public void readObject(InputStream input) throws CheckObjectException {

    }

    @Override
    public void writeObject(OutputStream output) throws CheckObjectException {

    }

    @Override
    public boolean checkObject() {
        return false;
    }
}
