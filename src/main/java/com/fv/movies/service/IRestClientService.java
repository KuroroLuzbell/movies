package com.fv.movies.service;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;


public interface IRestClientService {
    Uni<HttpResponse<Buffer>> sendPost(String strPayload, String strUrlAbs);
}
