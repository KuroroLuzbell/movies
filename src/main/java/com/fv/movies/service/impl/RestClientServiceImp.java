package com.fv.movies.service.impl;

import com.fv.movies.service.IRestClientService;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.vertx.mutiny.ext.web.client.WebClient;

@ApplicationScoped
public class RestClientServiceImp implements IRestClientService {

    private final WebClient objWebClient;

    Logger objLogger = Logger.getLogger(RestClientServiceImp.class.getName());

    @Inject
    public RestClientServiceImp(Vertx vertx) {
        this.objWebClient = WebClient.create(vertx);
    }

    public RestClientServiceImp(WebClient webClient) {
        this.objWebClient = webClient;
    }
    @Override
    public Uni<HttpResponse<Buffer>> sendPost(String strPayload, String strUrlAbs) {
        objLogger.info("Send request to  " + strUrlAbs);
        return objWebClient.postAbs(strUrlAbs)
                .putHeader("Content-Type", "application/json")
                .sendBuffer(Buffer.buffer(strPayload))
                .onItem().transform(resp ->  resp)
                .onFailure().invoke(e -> objLogger.info("Error during call the service " + e.getMessage()));
    }
}
