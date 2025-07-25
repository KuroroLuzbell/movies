package com.fv.movies.log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class CustomTrace {
    private String system;
    private String country;
    private String trackingId;
    private String url;
    private long totalTime;
    private List<DomainMessage> domainsMessages;

    public CustomTrace(String url) {
        this.trackingId = UUID.randomUUID().toString();
        this.url=url;
        this.domainsMessages=new ArrayList<>();
        this.system = ConfigProvider.getConfig().getValue("api.system", String.class);
        this.country= ConfigProvider.getConfig().getValue("api.country", String.class);
    }
}
