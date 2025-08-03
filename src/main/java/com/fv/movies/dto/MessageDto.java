package com.fv.movies.dto;


import java.io.Serial;
import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;


@XmlRootElement
@RegisterForReflection
public class MessageDto<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Setter
    @Getter
    private transient T object;

    @Getter
    private BaseExceptionDto status;

    public void setMessage(BaseExceptionDto status) {
        this.status = status;
    }

}
