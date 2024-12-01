package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class UserNotFoundByUsernameGiven extends RuntimeException {
    public UserNotFoundByUsernameGiven(String message) {
        super(message);
    }
}
