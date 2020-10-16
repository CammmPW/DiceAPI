package org.dicemc.diceapi.database;

public class DatabaseException extends Exception {
    private static final long serialVersionUID = -4155286232672183170L;

    private final String message;

    public DatabaseException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
