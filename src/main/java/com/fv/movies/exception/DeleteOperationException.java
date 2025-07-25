package com.fv.movies.exception;

public class DeleteOperationException  extends RuntimeException {
    public DeleteOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
