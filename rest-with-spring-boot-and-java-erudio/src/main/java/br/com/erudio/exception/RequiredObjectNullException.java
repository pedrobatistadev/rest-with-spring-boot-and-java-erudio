package br.com.erudio.exception;

public class RequiredObjectNullException extends RuntimeException {

    public RequiredObjectNullException() {
        super("it is not allowed to persist a null objects");
    }

    public RequiredObjectNullException(String message) {
        super(message);
    }

}
