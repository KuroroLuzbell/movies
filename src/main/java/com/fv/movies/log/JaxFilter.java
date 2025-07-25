package com.fv.movies.log;

import com.fv.movies.dto.MessageDto;
import com.fv.movies.service.ILogClientService;
import com.fv.movies.util.Constants;
import com.fv.movies.util.Converter;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

@Provider
public class JaxFilter implements ContainerResponseFilter, ContainerRequestFilter {

    private final ILogClientService objILogClientService;

    private long lngStartTime;

    private final ResourceInfo objResourceInfo;


    @Inject
    public JaxFilter(ILogClientService objILogClientService, ResourceInfo resourceInfo) {
        this.objILogClientService = objILogClientService;
        this.objResourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        lngStartTime = System.currentTimeMillis();
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        StringBuilder traceBuilder = new StringBuilder();
        Method objMethod = objResourceInfo.getResourceMethod();
        String strPathUrl = requestContext.getUriInfo().getPath();
        String strMethodHttp = requestContext.getMethod();
        String strController = objMethod.getDeclaringClass().getName();
        String strMethod = objMethod.getName();

        traceBuilder.append("Controller: ").append(strController).append("\n")
                .append("Verb: ").append(strMethodHttp).append("\n")
                .append("Method: ").append(strMethod).append("\n")
                .append("Result: ");
        CustomTrace objCustomTrace = new CustomTrace(strPathUrl);
        DomainMessage objDomainsMessages = new DomainMessage();
        Object objResponseEntity = responseContext.getEntity();
        MessageDto<?> objMessage = objResponseEntity instanceof MessageDto ? (MessageDto<?>) objResponseEntity : new MessageDto<>();
        try {
            String strCode = objMessage.getStatus().getCode();
            Object objMessageController = requestContext.getProperty(Constants.PROPERTY_MESSAGE);

            if (strCode.equals(Constants.SUCCESSFUL_RULE_CODE) || strCode.equals(Constants.NO_DATA_RULE_CODE)) {
                traceBuilder.append(objMessageController);
                objDomainsMessages.setMessage(traceBuilder.toString());
            } else {
                objDomainsMessages.setSeverity(Constants.SEVERITY_ERROR);
                objDomainsMessages.setMessage(objMessageController != null ? objMessageController.toString() : "Unknown error");
            }
        } catch (Exception ex) {
            objDomainsMessages.setMessage(Converter.convertToString(ex.getMessage()));
        } finally {
            long lngEndTime = System.currentTimeMillis() - lngStartTime;
            objCustomTrace.setTotalTime(lngEndTime);
            objDomainsMessages.setTimeElapsed(lngEndTime);
            objDomainsMessages.setFunction(strMethod);
            objCustomTrace.getDomainsMessages().add(objDomainsMessages);
            CompletableFuture.runAsync(() -> objILogClientService.sendLogs(objCustomTrace));
        }
    }

}