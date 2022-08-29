package lu.atozdigital.api.exception.business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmptyOrderException extends Exception{

    private static final long serialVersionUID = 1L;

    public EmptyOrderException(String message){
        super(message);
    }
}
