package pl.uginf.rcphrwebapp.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pl.uginf.rcphrwebapp.exceptions.InvoiceGenerationException;
import pl.uginf.rcphrwebapp.exceptions.NotFoundException;
import pl.uginf.rcphrwebapp.exceptions.UserNotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ValidationException.class, InvoiceGenerationException.class })
    protected ResponseEntity<Object> handleValidation(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { UserNotFoundException.class, NotFoundException.class })
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
