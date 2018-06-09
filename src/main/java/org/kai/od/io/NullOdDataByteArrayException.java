package org.kai.od.io;

public class NullOdDataByteArrayException extends IllegalArgumentException {
    public NullOdDataByteArrayException() {
        super();
    }

    public NullOdDataByteArrayException(String s) {
        super(s);
    }

    public NullOdDataByteArrayException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullOdDataByteArrayException(Throwable cause) {
        super(cause);
    }
}
