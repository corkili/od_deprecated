package org.kai.od.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class Category implements SerializableData {

    private Long id;

    private String name;

    private Map<Long, OpticalDevice> opticalDevices;

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

    public Map<Long, OpticalDevice> getOpticalDevices() {
        return opticalDevices;
    }

    public void setOpticalDevices(Map<Long, OpticalDevice> opticalDevices) {
        this.opticalDevices = opticalDevices;
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
