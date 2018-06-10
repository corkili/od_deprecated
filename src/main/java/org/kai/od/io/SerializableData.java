package org.kai.od.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializableData {

    Long getId();

    void setId(Long id);

    void readObject(InputStream input) throws CheckObjectException, IOException;

    void writeObject(OutputStream output) throws CheckObjectException, IOException;

    boolean checkObject();
}
