package com.fv.movies.service.impl;


import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
class RestClientServiceImplTest {

    @Test
    void testSendPost_Success() {

        WebClient webClient = Mockito.mock(WebClient.class);
        Buffer buffer = Mockito.mock(Buffer.class);
        HttpResponse<Buffer> httpResponse = Mockito.mock(HttpResponse.class);
        RestClientServiceImpl restClientServiceImpl = new RestClientServiceImpl(webClient);

        HttpRequest<Buffer> mockRequest = mock(HttpRequest.class);

        when(webClient.postAbs(anyString())).thenReturn(mockRequest);

        when(mockRequest.putHeader(anyString(), anyString())).thenReturn(mockRequest);

        when(mockRequest.sendBuffer(any())).thenReturn(Uni.createFrom().item(httpResponse));
        when(httpResponse.body()).thenReturn(buffer);
        when(httpResponse.statusCode()).thenReturn(200);

        Uni<HttpResponse<Buffer>> responseUni = restClientServiceImpl.sendPost("testPayload", "http://example.com");

        HttpResponse<Buffer> response = responseUni.await().indefinitely();
        assertEquals(200, response.statusCode());
        verify(webClient).postAbs("http://example.com");
    }
    @Test
    void testSendPost_Failure() {

        WebClient webClient = Mockito.mock(WebClient.class);


        HttpRequest<Buffer> mockRequest = Mockito.mock(HttpRequest.class);

        when(webClient.postAbs(anyString())).thenReturn(mockRequest);

        when(mockRequest.putHeader(anyString(), anyString())).thenReturn(mockRequest);

        when(mockRequest.sendBuffer(any()))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Test Failure")));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            invokeRestClient(webClient);
        });

        assertEquals("Test Failure", exception.getMessage());

        verify(webClient).postAbs("http://example.com");
    }
    private void invokeRestClient( WebClient webClient) {
        RestClientServiceImpl restClientServiceImpl = new RestClientServiceImpl(webClient);
        Uni<HttpResponse<Buffer>> responseUni = restClientServiceImpl.sendPost("testPayload", "http://example.com");
        responseUni.await().indefinitely();
    }

}