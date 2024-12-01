package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class UserAlreadyExistsInDatabaseException extends RuntimeException {
    public UserAlreadyExistsInDatabaseException(String message) {
        super(message);
    }
}
