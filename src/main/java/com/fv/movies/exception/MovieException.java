package com.fv.movies.exception;

public class MovieException extends RuntimeException{
    public MovieException(){super();}

    public MovieException(String message){
        super(message);
    }

    public MovieException(String message, Throwable objException){
        super(message, objException);
    }

    public MovieException(Throwable objException){
        super(objException);
    }
}
