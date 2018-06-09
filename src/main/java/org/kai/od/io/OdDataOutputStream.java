package org.kai.od.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class OdDataOutputStream extends OutputStream {

    private static final int DEFAULT_SIZE = 32;

    private byte[] data;

    private int pos;

    private int capacity;

    public OdDataOutputStream() {
        this.data = new byte[DEFAULT_SIZE];
        this.pos = 0;
        this.capacity = DEFAULT_SIZE;
    }

    public void writeTo(OutputStream output) {
        try {
            output.write(data, 0, pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(int b) {
        if (pos >= capacity) {
            ensureCapacity();
        }
        data[pos++] = (byte) b;
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        if (b == null) {
            return;
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            return;
        } else if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            write(b[off + i]);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    private void ensureCapacity() {
        this.data = Arrays.copyOf(this.data, this.data.length + DEFAULT_SIZE);
        this.capacity = this.data.length;
    }
}
