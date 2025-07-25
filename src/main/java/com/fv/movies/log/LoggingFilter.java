package com.fv.movies.log;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException; // Necesario para la firma del método
import java.util.logging.Logger;

@Provider
public class LoggingFilter implements ClientRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    // El método debe declarar "throws IOException"
    public void filter(ClientRequestContext requestContext) throws IOException {
        // Es buena práctica agregar un espacio para legibilidad
        LOGGER.info("Request URI: " + requestContext.getUri());
        LOGGER.info("Request headers: " + requestContext.getHeaders());
        LOGGER.info("Request method: " + requestContext.getMethod());

        if (requestContext.hasEntity()) {
            // El código original aquí tenía errores de sintaxis.
            // Simplemente registrar la entidad con toString() es más seguro para empezar.
            // Leer el cuerpo de la petición (entity) puede "consumirlo",
            // impidiendo que llegue al servidor real. Se necesita un manejo más avanzado
            // si quieres registrar el JSON completo.
            LOGGER.info("Request Entity: " + requestContext.getEntity());
        }
    }
}