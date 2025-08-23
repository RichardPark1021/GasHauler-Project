package controller;

import java.io.IOException;

public class GameException extends IOException {
    public GameException() {
        super();
    }

    public GameException(String message) {
        super(message);
    }
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
