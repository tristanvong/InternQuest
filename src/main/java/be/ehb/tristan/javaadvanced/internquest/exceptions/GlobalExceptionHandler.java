package be.ehb.tristan.javaadvanced.internquest.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleException(Exception exception, Model model){
        ModelAndView mav = new ModelAndView("error/general-error");
        mav.addObject("errorMessage", exception.getMessage());
        mav.addObject("exceptionName", exception.getClass().getSimpleName());
        return mav;
    }
}