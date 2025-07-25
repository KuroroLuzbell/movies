package com.fv.movies.util;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@RegisterForReflection
public class Process {
    private Process() {
    }

    public static <T> Response processSuccess(T object) {
        return Response.ok().entity(object).build();
    }


    public static Response processWithoutContent(Object object) {
        return Response.noContent().entity(object).build();
    }

    public static Response processWithoutImplementation(Object object) {
        return Response.status(Status.NOT_IMPLEMENTED).entity(object).build();
    }

    public static Response processError(Object object) {
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(object).build();
    }

    public static Response processBadRequest(Object object) {
        return Response.status(Status.BAD_REQUEST).entity(object).build();
    }
    public static Response handleJsonProcessingException(Throwable ex ) {
        var objMessage = Message.creatApiErrorMessage(ex,ex.getMessage());
        return Process.processBadRequest(objMessage);
    }

    public static Response handleGeneralException(Throwable e) {
        var objMessage = Message.creatApiErrorMessage(e, e.getMessage());
        return Process.processError(objMessage);
    }

    public static Response processNotFound(Object object) {
        return Response.status(Status.NOT_FOUND).entity(object).build();
    }

    public static Response processServiceUnavailable(Object object) {
        return Response.status(Status.SERVICE_UNAVAILABLE).entity(object).build();
    }

    public static Response handleKnownExceptions(Throwable e) {
        Object objMessage = Message.creatApiErrorMessage(null, e.getMessage());
        if(e instanceof IllegalArgumentException ) {
            return processBadRequest(objMessage);
        } else if (e instanceof NotFoundException) {
            return processNotFound(objMessage);
        } else if (e instanceof ServiceUnavailableException) {
            return processServiceUnavailable(objMessage);
        }
        return processError(objMessage);
    }

}

