package com.fv.movies.service.impl;

import com.fv.movies.exception.LogClientException;
import com.fv.movies.service.ILogClientService;
import com.fv.movies.service.IRestClientService;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;


@ApplicationScoped
@Default
public class LogClientServiceImp implements ILogClientService {

    @ConfigProperty(name = "log.service",defaultValue = "")
    String strUlrServiceLog;

    Logger objLogger  = Logger.getLogger(LogClientServiceImp.class.getName());

    final IRestClientService objRestClientService;

    @Inject
    public LogClientServiceImp(IRestClientService objRestClientService) {
        this.objRestClientService = objRestClientService;
    }

    @Override
    public Uni<String> sendLogs(String strCustomTrace) {
        try {
            objLogger.info(strCustomTrace);
            return  objRestClientService.sendPost(strCustomTrace,strUlrServiceLog).onFailure().invoke(throwable -> objLogger.info("Error processing message "+ throwable.getMessage()))
                    .onItem().transform(response -> {
                        objLogger.info(response.bodyAsString());
                        return response.bodyAsString();
                    });

        } catch (Exception objEx) {
            throw new LogClientException("Error processing message", objEx);
        }
    }

    @Override
    public void sendLogs(Object objRequestBody) {
        String objResponse = sendLogs(JsonObject.mapFrom(objRequestBody).toString()).await().indefinitely();
        objLogger.info(objResponse);
    }

}
