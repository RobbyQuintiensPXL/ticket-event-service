package be.jevent.eventservice.controller.controlleradvice;

import be.jevent.eventservice.exception.EventException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EventControllerAdvice {

    @ResponseBody
    @ExceptionHandler(EventException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String notFoundHandler(EventException ex) {
        return ex.getMessage();
    }
}
