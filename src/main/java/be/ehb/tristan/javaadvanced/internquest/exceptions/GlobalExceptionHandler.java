package be.ehb.tristan.javaadvanced.internquest.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserCreationException.class)
    public String handleUserCreationException(UserCreationException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("exceptionName", exception.getClass().getSimpleName());
        return "error/error-creation-account";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRunTimeException(RuntimeException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("exceptionName", exception.getClass().getSimpleName());
        return "error/general-error";
    }
}