package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class UserNotFoundByIdGivenException extends RuntimeException {
    public UserNotFoundByIdGivenException(String message) {
        super(message);
    }
}
