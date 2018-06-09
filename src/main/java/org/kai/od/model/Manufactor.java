package org.kai.od.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.kai.od.io.CheckObjectException;
import org.kai.od.io.SerializableData;

public class Manufactor implements SerializableData {

    private Long id;

    private String name;

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
