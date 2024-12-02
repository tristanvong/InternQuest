package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class UserNotFoundByUsernameGivenException extends RuntimeException {
    public UserNotFoundByUsernameGivenException(String message) {
        super(message);
    }
}
