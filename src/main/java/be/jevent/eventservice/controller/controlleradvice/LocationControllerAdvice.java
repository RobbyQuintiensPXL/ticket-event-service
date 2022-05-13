package be.jevent.eventservice.controller.controlleradvice;

import be.jevent.eventservice.exception.LocationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LocationControllerAdvice {

    @ResponseBody
    @ExceptionHandler(LocationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notFoundHandler(LocationException ex) {
        return ex.getMessage();
    }
}
