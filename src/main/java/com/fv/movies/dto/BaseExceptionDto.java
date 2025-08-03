package com.fv.movies.dto;

import java.io.Serial;
import java.io.Serializable;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement
@RegisterForReflection
@Data
public class BaseExceptionDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;
    private String code;

}