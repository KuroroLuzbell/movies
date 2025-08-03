package com.fv.movies.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fv.movies.dto.PagedResponseDTO;
import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.exception.MovieException;
import com.fv.movies.service.IGenderService;
import com.fv.movies.util.Message;
import com.fv.movies.util.Constants;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fv.movies.util.Process;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


@Path("/genders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenderController {

    IGenderService genderService;

    @Inject
    public GenderController(IGenderService genderService) {
        this.genderService = genderService;
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get next folio by document by type id", description = "Returns the next folio for a given document type")
    @APIResponse(responseCode = "200", description = "Folio returned successfully")
    @APIResponse(responseCode = "400", description = "Invalid document type ID")
    @APIResponse(responseCode = "404", description = "No records found response")
    @APIResponse(responseCode = "503", description = "Service unavailable response")
    public Uni<Response> getById(@PathParam("id") long id,
                                       @Context ContainerRequestContext context) {
        context.setProperty("message", "Request folio for documentTypeId: ");

        return genderService.getGenderById(id)
                .onItem()
                .transform(genderDTO -> genderDTO != null ? Process.processSuccess(genderDTO) : Process.processNotFound(Response.Status.NOT_FOUND))
                .onFailure()
                .recoverWithItem(Process::handleKnownExceptions);

    }

    @GET
    @Operation(summary = "Get next folio by document by type id", description = "Returns the next folio for a given document type")
    @APIResponse(responseCode = "200", description = "Folio returned successfully")
    @APIResponse(responseCode = "400", description = "Invalid document type ID")
    @APIResponse(responseCode = "404", description = "No records found response")
    @APIResponse(responseCode = "503", description = "Service unavailable response")
    public Uni<Response> getAll(@Context ContainerRequestContext context) {
        context.setProperty("message", "Request folio for documentTypeId: ");

        return genderService.getAllGenders()
                .onItem()
                .transform(genderDTO -> genderDTO != null ? Process.processSuccess(genderDTO) : Process.processNotFound(Response.Status.NOT_FOUND))
                .onFailure()
                .recoverWithItem(Process::handleKnownExceptions);

    }

    @GET
    @Path("/paginated") // <-- Una ruta clara para el endpoint paginado
    @Operation(summary = "Get a paginated list of genders",
            description = "Returns a paginated list of all available movie genders, allowing for efficient data retrieval.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Paginated list of genders returned successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PagedResponseDTO.class)) // <-- Documenta el DTO de respuesta
            ),
            @APIResponse(
                    responseCode = "503",
                    description = "Service unavailable."
            )
    })
    public Uni<Response> getPaginatedGenders(
            @Parameter(description = "The page number to retrieve (0-indexed).", required = false)
            @QueryParam("page") @DefaultValue("0") int page,

            @Parameter(description = "The number of items per page.", required = false)
            @QueryParam("size") @DefaultValue("10") int size) {

        return genderService.getGendersPaginated(page, size)
                .onItem().transform(pagedResponse -> Response.ok(pagedResponse).build())
                .onFailure().recoverWithItem(Process::handleKnownExceptions);
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "receipt of adjustments", description = "this service is about receipt of adjustments ")
    @APIResponse(responseCode = "200", description = "This service is responding correctly")
    @APIResponse(responseCode = "404", description = "Service no response")

    public Uni<Response> save(@Valid GenderDTO genderDto,
                                  @Context ContainerRequestContext objRequestContext) {

        objRequestContext.setProperty(Constants.PROPERTY_MESSAGE, "receipt least  of adjustments");
        return genderService
                .createGender(genderDto)
                .onItem()
                .transformToUni(
                        voidItem -> Uni.createFrom().item(Process.processSuccess(Message.createSuccessfulMessage(null))))
                .onFailure(JsonProcessingException.class)
                .recoverWithItem(Process::handleJsonProcessingException)
                .onFailure()
                .recoverWithItem(Process::handleGeneralException);

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "receipt of adjustments", description = "this service is about receipt of adjustments ")
    @APIResponse(responseCode = "200", description = "This service is responding correctly")
    @APIResponse(responseCode = "404", description = "Service no response")
    public Uni<Response> update(@PathParam("id")  Long id,@Valid GenderDTO genderDto,
                              @Context ContainerRequestContext objRequestContext) {
        objRequestContext.setProperty(Constants.PROPERTY_MESSAGE, "receipt least  of adjustments");
        return genderService
                .updateGender(id,genderDto)
                .onItem()
                .transformToUni(
                        voidItem -> Uni.createFrom().item(Process.processSuccess(Message.createSuccessfulMessage(null))))
                .onFailure(JsonProcessingException.class)
                .recoverWithItem(Process::handleJsonProcessingException)
                .onFailure()
                .recoverWithItem(Process::handleGeneralException);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(
            @Parameter(description = "id")
            @QueryParam("id")  Long id,
            @Context ContainerRequestContext objRequestContext) {
        objRequestContext.setProperty(Constants.PROPERTY_MESSAGE, "delete adjustment");

        return genderService.deleteGender(id)
                .onItem()
                .transformToUni(
                        voidItem -> Uni.createFrom().item(Process.processSuccess(Message.createSuccessfulMessage(null))))
                .onFailure(MovieException.class)
                .recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                        .entity(Message.creatApiErrorMessage(null, e.getMessage())).build())
                .onFailure(JsonProcessingException.class)
                .recoverWithItem(Process::handleJsonProcessingException)
                .onFailure()
                .recoverWithItem(Process::handleGeneralException);
    }
}
