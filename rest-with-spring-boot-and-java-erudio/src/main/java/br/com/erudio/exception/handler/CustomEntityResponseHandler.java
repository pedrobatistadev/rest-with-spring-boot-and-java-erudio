package br.com.erudio.exception.handler;


import br.com.erudio.exception.ExceptionResponse;
import br.com.erudio.exception.RequiredObjectNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
@RestController
public class CustomEntityResponseHandler {

   private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handlerAllException(Exception ex, WebRequest request) {
        String data = sdf.format(new Date());
        ExceptionResponse response = new ExceptionResponse(data, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handlerBadRequestException(Exception ex, WebRequest request) {
        String data = sdf.format(new Date());
        ExceptionResponse response = new ExceptionResponse(data, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectNullException.class)
    public final ResponseEntity<ExceptionResponse> handlerNullObject(Exception ex, WebRequest request) {
        String data = sdf.format(new Date());
        ExceptionResponse response = new ExceptionResponse(data, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}


