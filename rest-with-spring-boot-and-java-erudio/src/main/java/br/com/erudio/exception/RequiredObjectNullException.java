package br.com.erudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectNullException extends RuntimeException {

    public RequiredObjectNullException() {
        super("it is not allowed to persist a null objects");
    }

    public RequiredObjectNullException(String message) {
        super(message);
    }

}
