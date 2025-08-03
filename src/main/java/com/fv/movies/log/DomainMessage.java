package com.fv.movies.log;

import com.fv.movies.util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Data
@NoArgsConstructor
public class DomainMessage {

    private String severity;
    private String message;
    private long timeElapsed;
    private String function;

}
