package br.com.erudio.exception;

import java.util.Date;

public record ExceptionResponse(Date data, String message, String details) {
}
