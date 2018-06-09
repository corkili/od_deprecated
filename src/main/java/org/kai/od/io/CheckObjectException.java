package org.kai.od.io;

public class CheckObjectException extends Exception {
    public CheckObjectException() {
        super();
    }

    public CheckObjectException(String message) {
        super(message);
    }

    public CheckObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckObjectException(Throwable cause) {
        super(cause);
    }
}
