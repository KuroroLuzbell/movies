package com.fv.movies.service.impl;
import com.fv.movies.service.IRestClientService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class LogClientServiceImplTest {
    @Inject
    LogClientServiceImpl logClientService;

    @InjectMock
    IRestClientService objRestClientServiceMock;

    @Test
    void testSendLogs_String_Success() {

        String logRequestBody ="{data:data}";
        String responseBody = "Success";

        HttpResponse<Buffer> mockResponse = mock(HttpResponse.class);
        when(mockResponse.bodyAsString()).thenReturn(responseBody);
        when(objRestClientServiceMock.sendPost(anyString(),anyString()))
                .thenReturn(Uni.createFrom().item(mockResponse));

        logClientService.sendLogs(logRequestBody);

        verify(objRestClientServiceMock, times(1)).sendPost(anyString(),anyString());
    }

    @Test
    void testSendLogs_String_Failure() {
        String logMessage = "Test log message";
        Exception exception = new Exception("Simulated failure");
        String responseBody = "Error";
        HttpResponse<Buffer> mockResponse = mock(HttpResponse.class);
        when(mockResponse.bodyAsString()).thenReturn(responseBody);
        when(objRestClientServiceMock.sendPost(anyString(),anyString())).thenReturn(Uni.createFrom().failure(exception));

        Exception thrown =assertThrows(Exception.class, () -> {
            logClientService.sendLogs(logMessage).await().indefinitely();
        });

        assertEquals("java.lang.Exception: Simulated failure", thrown.getMessage());
        verify(objRestClientServiceMock, times(1)).sendPost(anyString(),anyString());
    }

    @Test
    void testSendLogs_Object_Success() {
        TestRequestBody objRequestBody = new TestRequestBody("Test body content");
        String logMessage = "{\"content\":\"Test body content\"}";

        HttpResponse<Buffer> mockResponse = mock(HttpResponse.class);
        when(mockResponse.bodyAsString()).thenReturn(logMessage);
        when(objRestClientServiceMock.sendPost(anyString(),anyString()))
                .thenReturn(Uni.createFrom().item(mockResponse));

        logClientService.sendLogs(objRequestBody);
        verify(objRestClientServiceMock, times(1)).sendPost(anyString(),anyString());
    }

    public static class TestRequestBody {
        public String content;
        public TestRequestBody(String content) {
            this.content = content;
        }
    }

}
