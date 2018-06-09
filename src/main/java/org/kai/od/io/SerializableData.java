package org.kai.od.io;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializableData {
    void readObject(InputStream input) throws CheckObjectException;

    void writeObject(OutputStream output) throws CheckObjectException;

    boolean checkObject();
}
