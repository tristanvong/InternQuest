package be.ehb.tristan.javaadvanced.internquest.exceptions;

public class CompanyNotFoundByIdException extends RuntimeException {
    public CompanyNotFoundByIdException(String message) {
        super(message);
    }
}
