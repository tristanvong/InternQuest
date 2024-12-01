package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class UserNotFoundByIdGiven extends RuntimeException {
    public UserNotFoundByIdGiven(String message) {
        super(message);
    }
}
