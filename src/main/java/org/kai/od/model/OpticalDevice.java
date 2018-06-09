package org.kai.od.model;

import java.io.IOException;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TopCategory getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(TopCategory topCategory) {
        this.topCategory = topCategory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Map<Long, Manufactor> getRepresentativeManufactors() {
        return representativeManufactors;
    }

    public void setRepresentativeManufactors(Map<Long, Manufactor> representativeManufactors) {
        this.representativeManufactors = representativeManufactors;
    }

    @Override
    public void readObject(InputStream input) throws CheckObjectException, IOException {

    }

    @Override
    public void writeObject(OutputStream output) throws CheckObjectException, IOException {

    }

    @Override
    public boolean checkObject() {
        return false;
    }
}
