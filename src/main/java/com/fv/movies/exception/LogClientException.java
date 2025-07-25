package com.fv.movies.exception;

public class LogClientException extends RuntimeException {
    public LogClientException(){super();}
    public LogClientException(String message){super(message);}
    public LogClientException(String message, Throwable cause){super(message,cause);}
    public LogClientException(Throwable cause){super(cause);}
}