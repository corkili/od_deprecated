package org.kai.od.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class Category implements SerializableData {

    private Long id;

    private String name;

    private Map<Long, OpticalDevice> opticalDevices;

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
