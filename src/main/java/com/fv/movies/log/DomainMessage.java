package com.fv.movies.log;

import com.fv.movies.util.Constants;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class DomainMessage {
    private String domain;
    private String function;
    private long timeElapsed;
    private String severity;
    private String message;

    public DomainMessage(){
        this.severity= Constants.SEVERITY_INFO;
        this.domain=ConfigProvider.getConfig().getValue("api.domain", String.class);
    }
}
