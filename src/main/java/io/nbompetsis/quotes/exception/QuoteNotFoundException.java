package io.nbompetsis.quotes.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class QuoteNotFoundException extends RuntimeException {
    public QuoteNotFoundException() {
        super();
    }
    public QuoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public QuoteNotFoundException(String message) {
        super(message);
    }
    public QuoteNotFoundException(Throwable cause) {
        super(cause);
    }

}
