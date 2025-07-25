package com.fv.movies.util;

import com.fv.movies.dto.MessageDto;
import com.fv.movies.dto.BaseExceptionDto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Message {
    private Message() {
    }

    public static <T> MessageDto<T> createSuccessfulMessage(T object) {

        BaseExceptionDto objEx = new BaseExceptionDto();

        MessageDto<T> objMessage = new MessageDto<>();

        objEx.setCode(Constants.SUCCESSFUL_RULE_CODE);

        objEx.setMessage(Constants.SUCCESSFUL_RULE_MESSAGE);

        objMessage.setObject(object);

        objMessage.setMessage(objEx);

        return objMessage;
    }

    public static <T> MessageDto<T> createUnsuccessfulMessage(T object) {

        BaseExceptionDto objEx = new BaseExceptionDto();

        MessageDto<T> objMessage = new MessageDto<>();

        objEx.setCode(Constants.NO_DATA_RULE_CODE);

        objEx.setMessage(Constants.UNSUCCESSFUL_RULE_MESSAGE);

        objMessage.setMessage(objEx);

        objMessage.setObject(object);

        return objMessage;
    }

    public static <T> MessageDto<T> creatApiErrorMessage(T object,String strMessage) {

        BaseExceptionDto objEx = new BaseExceptionDto();

        MessageDto<T> objMessage = new MessageDto<>();

        objEx.setCode(Constants.API_ERROR_CODE);


        objEx.setMessage(Constants.API_ERROR_MESSAGE + " " + strMessage);

        objMessage.setObject(object);
        objMessage.setMessage(objEx);
        return objMessage;
    }

}
