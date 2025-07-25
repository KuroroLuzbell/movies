package com.fv.movies.util;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection

public class Constants {
    private Constants() {
    }

    public static final int ZERO = 0;

    // Response code assigned by regulation, cannot be used for other purposes
    public static final String SUCCESSFUL_RULE_CODE = "001";
    public static final String NO_DATA_RULE_CODE = "002";
    public static final String API_ERROR_CODE = "003";

    // Global response messages
    public static final String SUCCESSFUL_RULE_MESSAGE = "All rules were executed successfully";
    public static final String UNSUCCESSFUL_RULE_MESSAGE = "One or more rules could not be fulfilled correctly";
    public static final String API_ERROR_MESSAGE = "Service error";

    public static final String PROPERTY_MESSAGE = "message";

    // Severidad
    public static final String SEVERITY_INFO = "INFO";
    public static final String SEVERITY_ERROR = "ERROR";
    // Querys
    public static final String QUERY_INCREMENT_AND_GET_BY_DOCUMENT_TYPE = """
                     UPDATE fd
                     SET fd.Folio = fd.Folio + 1
                     OUTPUT inserted.Folio
                     FROM Suscripcion.dbo.FolioDocumentos fd
                     WHERE fd.idTipoDocumento = @p1
                     """;

    public static final String QUERY_INSERT_LOGS_FOLIO = """
                     INSERT INTO Suscripcion.dbo.FolioLogs VALUES (@p1,@p2, GETDATE())
                     """;

    public static final String QUERY_ACTIVATE_LIQUIDATION = """
                     EXEC dbo.full_pa_ActivaLiquidadorPoliza_Actualizar @p1
                      """;

    public static final String QUERY_ACTIVATE = """
                     EXEC dbo.pa_grabaPolizaAlta @p1, @p2, @p3
                      """;

    public static final String QUERY_UPDATE_CONTRACT_SERVICE = """
                     EXEC dbo.pa_grabaservicios @p1, @p2, @p3, @p4
                      """;


}