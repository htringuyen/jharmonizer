package io.javaside.jharmonizer;

public class JHarmonizerException extends RuntimeException {

    public JHarmonizerException(String message) {
        super(message);
    }

    public JHarmonizerException(Throwable cause) {
        super(cause);
    }

    public JHarmonizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JHarmonizerException() {
        super();
    }
}
