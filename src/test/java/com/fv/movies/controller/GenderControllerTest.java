package com.fv.movies.controller;

import com.fv.movies.dto.PagedResponseDTO;
import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.exception.MovieException;
import com.fv.movies.service.IGenderService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(GenderController.class)
@ExtendWith(InstancioExtension.class)
class GenderControllerTest {

    @InjectMock
    IGenderService mockGenderService;

    @InjectMock
    ContainerRequestContext requestContext;

    @Inject
    GenderController genderController;

    @BeforeEach
    void setup() {
        Mockito.reset(mockGenderService);
    }

    @Nested
    @RunOnVertxContext
    @DisplayName("GET /genders/{id}")
    class GetGenderById {

        @Test
        @DisplayName("Should return 200 with entity")
        void getGenderById_Success() {
            GenderDTO dto = new GenderDTO();
            Mockito.when(mockGenderService.getGenderById(Mockito.anyLong()))
                    .thenReturn(Uni.createFrom().item(dto));
            given()
                    .queryParam("id", 3)
                    .when()
                    .get("/genders/3")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("size()", is(3));
        }

        @Test
        @DisplayName("Should return 404 not found")
        void getGenderById_NotFound() {
            long nonExistentId = 999L;

            given()
                    .pathParam("id", nonExistentId)
                    .when()
                    .get("/genders/{id}")
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /genders")
    class GetAll {

        @Test
        @DisplayName("Should return 200 OK with a list of genders")
        void getGenderById_Success() {
            GenderDTO dto = new GenderDTO();
            Mockito.when(mockGenderService.getAllGenders())
                    .thenReturn(Uni.createFrom().item(List.of(dto)));
            given()
                    .when()
                    .get("/genders")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("size()", is(1));
        }
    }

    @Nested
    @DisplayName("GET /genders/paginated")
    class GetPaginatedGenders {

        @Test
        @DisplayName("Should return 200 OK with a paginated response")
        void getPaginatedGenders_Success() {
            // CORREGIDO: Usar el constructor correcto para PagedResponseDTO
            List<GenderDTO> content = Collections.singletonList(new GenderDTO(3L, "ciencia", LocalDateTime.now()));
            PagedResponseDTO<GenderDTO> pagedResponse = new PagedResponseDTO<>(content, 0, 10, 1L, 1);
            when(mockGenderService.getGendersPaginated(anyInt(), anyInt())).thenReturn(Uni.createFrom().item(pagedResponse));

            given()
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .when().get("/genders/paginated")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("content[0].description", is("ciencia"))
                    .body("totalPages", is(1))
                    .body("currentPage", is(0));
        }
    }

    @Nested
    @DisplayName("POST /genders")
    class SaveGender {

        @Test
        @DisplayName("Should return 200 OK on successful creation")
        void saveGender_Success() {
            GenderDTO validList = new GenderDTO();
            validList.setCreateAt(LocalDateTime.now());
            validList.setDescription("oo");
            validList.setId(1L);


            when(mockGenderService.createGender(validList))
                    .thenReturn(Uni.createFrom().nullItem());

            // Act
            Response response = genderController
                    .save(validList, requestContext)
                    .await().indefinitely();

            // Assert
            assertEquals(200, response.getStatus());
        }

        @Test
        @DisplayName("Should return 400 Bad Request for invalid DTO")
        void saveGender_InvalidDTO() {
            // La anotación @Valid de Quarkus se encarga de esto automáticamente
            GenderDTO invalidDto = new GenderDTO();
            invalidDto.setId(2L); // Asumiendo que 'name' es requerido (ej. @NotBlank)

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidDto)
                    .when().post("/genders")
                    .then()
                    .statusCode(400);
        }


    }

    @Nested
    @DisplayName("PUT /genders/{id}")
    class UpdateGender {

        @Test
        @DisplayName("Should return 200 OK on successful update")
        void updateGender_Success() {
            GenderDTO validList = new GenderDTO();
            validList.setCreateAt(LocalDateTime.now());
            validList.setDescription("oo");
            validList.setId(3L);


            when(mockGenderService.updateGender(3L,validList))
                    .thenReturn(Uni.createFrom().nullItem());

            // Act
            Response response = genderController
                    .update(3L,validList, requestContext)
                    .await().indefinitely();

            // Assert
            assertEquals(200, response.getStatus());
        }
    }

    @Nested
    @DisplayName("DELETE /genders")
    class DeleteGender {

        @Test
        @DisplayName("Should return 200 OK on successful deletion")
        void deleteGender_Success() {
            // CORRECTO: deleteGender devuelve Uni<Void>, por lo que voidItem() es correcto aquí.

            when(mockGenderService.deleteGender(3L))
                    .thenReturn(Uni.createFrom().nullItem());

            // Act
            Response response = genderController
                    .delete(3L, requestContext)
                    .await().indefinitely();

            // Assert
            assertEquals(200, response.getStatus());
        }
        @Test
        @DisplayName("Should return 404 not found")
        void deleteGender_NotFound() {

            when(mockGenderService.deleteGender(1L))
                    .thenReturn(Uni.createFrom().failure(new MovieException("Gender not found")));


            given()
                    .when()
                    .delete("/1")
                    .then()
                    .statusCode(Response.Status.NOT_FOUND.getStatusCode());
        }

    }
}