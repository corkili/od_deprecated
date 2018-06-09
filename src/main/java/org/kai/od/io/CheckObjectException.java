package org.kai.od.io;

import java.io.IOException;

public class CheckObjectException extends IOException {
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
