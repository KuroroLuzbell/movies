package com.fv.movies.service;

import io.smallrye.mutiny.Uni;


public interface ILogClientService {

    Uni<String> sendLogs(String strCustomTrace);

    void sendLogs(Object objRequestBody);
}
