package be.ehb.tristan.javaadvanced.internquest.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundByIdGiven.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundByIdGiven exception, Model model) {
        ModelAndView mav = new ModelAndView("user/user-not-found");
        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }

    @ExceptionHandler(UserAlreadyExistsInDatabaseException.class)
    public ModelAndView handleUserAlreadyExistsInDatabaseException(UserAlreadyExistsInDatabaseException exception, Model model) {
        ModelAndView mav = new ModelAndView("user/user-already-exists");
        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }
}