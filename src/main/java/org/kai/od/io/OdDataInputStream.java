package org.kai.od.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class OdDataInputStream extends InputStream {

    public static final int ERROR_VALUE = Byte.valueOf(Byte.MIN_VALUE).intValue() - 1;

    private byte[] data;

    private int pos;

    private int count;

    public OdDataInputStream(byte[] data) throws NullOdDataByteArrayException {
        this(data, 0, data.length);
    }

    public OdDataInputStream(byte[] data, int pos, int count) throws NullOdDataByteArrayException {
        if (data == null) {
            this.data = new byte[0];
            this.pos = 0;
            this.count = 0;
            throw new NullOdDataByteArrayException("The Argument \"data\" should not be null!");
        } else {
            this.data = Arrays.copyOf(data, data.length);
            this.pos = pos;
            this.count = count;
        }
    }

    @Override
    public int read() {
        return (pos < count) ? (data[pos++] & 0xff) : ERROR_VALUE;
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        if (b == null) {
            return -1;
        } else if (off < 0 || len < 0 || len > b.length - off) {
            return -1;
        } else if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == ERROR_VALUE) {
            return -1;
        }

        b[off] = (byte) c;

        int i = 1;
        for (; i < len ; i++) {
            c = read();
            if (c == ERROR_VALUE) {
                break;
            }
            b[off + i] = (byte)c;
        }
        return i;
    }

    @Override
    public long skip(long n)  {
        return 0;
    }

    @Override
    public int available() throws IOException {
        return count - pos;
    }

    @Override
    public void close() {
    }

    @Override
    public synchronized void mark(int readlimit) {
    }

    @Override
    public synchronized void reset() {
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
