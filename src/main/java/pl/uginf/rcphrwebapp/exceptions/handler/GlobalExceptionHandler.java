package pl.uginf.rcphrwebapp.exceptions.handler;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pl.uginf.rcphrwebapp.exceptions.NotFoundException;
import pl.uginf.rcphrwebapp.exceptions.UserNotFoundException;
import pl.uginf.rcphrwebapp.exceptions.ValidationException;
import pl.uginf.rcphrwebapp.exceptions.WrongFileExtensionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = { ValidationException.class })
    protected ResponseEntity<Object> handleValidation(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = { UserNotFoundException.class, NotFoundException.class })
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = WrongFileExtensionException.class)
    protected ResponseEntity<Object> handleWrongFileExtension(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    protected ResponseEntity<Object> handleDuplicateKey(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
