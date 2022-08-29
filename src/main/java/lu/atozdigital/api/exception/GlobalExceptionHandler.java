package lu.atozdigital.api.exception;
import lu.atozdigital.api.exception.application.ResourceNotFoundException;
import lu.atozdigital.api.exception.business.EmptyOrderException;
import lu.atozdigital.api.exception.business.QuantityInsufficientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import javax.naming.SizeLimitExceededException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("RNF",new Date(), ex.getMessage(), request.getDescription(false),HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("IA",new Date(), ex.getMessage(), request.getDescription(false),HttpStatus.EXPECTATION_FAILED.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<?> sizeLimitExceededException(SizeLimitExceededException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("MPE",new Date(), ex.getMessage(), request.getDescription(false),HttpStatus.EXPECTATION_FAILED.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(QuantityInsufficientException.class)
    public ResponseEntity<?> quantityInsufficientException(QuantityInsufficientException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("QI",new Date(), ex.getMessage(), request.getDescription(false),HttpStatus.EXPECTATION_FAILED.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(EmptyOrderException.class)
    public ResponseEntity<?> emptyOrderException(EmptyOrderException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("QI",new Date(), ex.getMessage(), request.getDescription(false),HttpStatus.EXPECTATION_FAILED.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails("BR",new Date(), "", request.getDescription(false),HttpStatus.EXPECTATION_FAILED.toString());
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorDetails.setMessage(errorDetails.getMessage()+error.getDefaultMessage()+"\n");
        });
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }
}
