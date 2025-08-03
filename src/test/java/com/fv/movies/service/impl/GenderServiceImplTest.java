package com.fv.movies.service.impl;

import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.entity.Gender;
import com.fv.movies.mapper.IGenderMapper;
import com.fv.movies.repository.impl.GenderRepository;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GenderServiceImplTest {

    @Mock
    GenderRepository genderRepository;

    @Mock
    IGenderMapper genderMapper;

    @InjectMocks
    GenderServiceImpl genderService;


    // --- ESTAS PRUEBAS ESTÁN BIEN PORQUE USAN @WithSession (NO TRANSACCIONAL) ---
    @Nested
    @DisplayName("Get All Genders")
    class GetAllGendersTests {
        @Test
        @DisplayName("Should return a list of all gender DTOs")
        void getAllGenders() {

            // Arrange
            List<Gender> genderEntities = List.of(new Gender(3L, "Action", LocalDateTime.now()));
            List<GenderDTO> genderDTOs = List.of(new GenderDTO(3L, "ciencia", LocalDateTime.now()));

            when(genderRepository.listAll()).thenReturn(Uni.createFrom().item(genderEntities));
            when(genderMapper.toDTOList(genderEntities)).thenReturn(genderDTOs);

            // Act
            List<GenderDTO> result = genderService.getAllGenders().await().indefinitely();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals("ciencia", result.get(0).getDescription());
        }

    }

    @Nested
    @DisplayName("Get Gender By ID")
    class GetGenderByIdTests {
        @Test
        @DisplayName("Should return GenderDTO when gender is found")
        void getGenderById_whenFound_shouldReturnGenderDTO() {
            Gender mockEntities = new Gender(1L, "Action",LocalDateTime.now());
            GenderDTO expected = new GenderDTO(1L, "Action",LocalDateTime.now());

            when(genderRepository.findById(1L)).thenReturn(Uni.createFrom().item(mockEntities));
            when(genderMapper.toDTO(mockEntities)).thenReturn(expected);

            // ⚠️ Aquí no se necesita Vert.x manualmente
            GenderDTO result = genderService.getGenderById(1L).await().indefinitely();
            assertEquals(expected, result);
        }

    }
/*
    @Nested
    @DisplayName("Create Gender")
    @RunOnVertxContext // <-- CORRECCIÓN: Añadir contexto para el método transaccional
    class CreateGenderTests {
        @Test
        @DisplayName("Should create and return a new GenderDTO")
        void createGender_shouldCreateAndReturnNewGenderDTO() {
            // Arrange
            when(genderMapper.toEntity(genderDTO)).thenReturn(gender);
            when(genderRepository.persistAndFlush(gender)).thenReturn(Uni.createFrom().item(gender));
            when(genderMapper.toDTO(gender)).thenReturn(genderDTO);

            // Act & Assert: Usar el patrón no bloqueante
            UniAssertSubscriber<GenderDTO> subscriber = genderService.createGender(genderDTO)
                    .subscribe().withSubscriber(UniAssertSubscriber.create());

            GenderDTO result = subscriber.awaitItem(Duration.ofSeconds(5)).getItem();
            assertEquals(genderDTO, result);
            verify(genderMapper).toEntity(genderDTO);
            verify(genderRepository).persistAndFlush(gender);
            verify(genderMapper).toDTO(gender);
        }
    }

    // --- ESTAS PRUEBAS NECESITAN LA CORRECCIÓN PORQUE updateGender ES TRANSACCIONAL ---
    @Nested
    @DisplayName("Update Gender")
    @RunOnVertxContext // <-- CORRECCIÓN: Añadir contexto para el método transaccional
    class UpdateGenderTests {
        @Test
        @DisplayName("Should update and return GenderDTO when gender exists")
        void updateGender_whenExists_shouldUpdateAndReturnGenderDTO() {
            // Arrange
            GenderDTO updatedDto = new GenderDTO(1L, "Action-Updated", null);
            when(genderRepository.findById(1L)).thenReturn(Uni.createFrom().item(gender));
            when(genderMapper.toDTO(gender)).thenReturn(updatedDto);

            // Act & Assert: Usar el patrón no bloqueante
            UniAssertSubscriber<GenderDTO> subscriber = genderService.updateGender(1L, updatedDto)
                    .subscribe().withSubscriber(UniAssertSubscriber.create());

            GenderDTO result = subscriber.awaitItem(Duration.ofSeconds(5)).getItem();
            assertEquals(updatedDto, result);
            verify(genderRepository).findById(1L);
            verify(genderMapper).updateEntityFromDto(updatedDto, gender);
            verify(genderMapper).toDTO(gender);
        }
    }

    // --- ESTA PRUEBA YA ESTABA CORRECTA ---
    @Nested
    @DisplayName("Delete Gender")
    @RunOnVertxContext
    class DeleteGenderTests {
        @Test
        @DisplayName("Should complete successfully when deletion is successful")
        void deleteGender_Success() {
            genderService.deleteGender(1L)
                    .subscribe().with(
                            Assertions::assertNull
                            //failure -> fail("Should not have failed")
                    );
        }
    }

    // --- ESTA PRUEBA ESTÁ BIEN PORQUE USA @WithSession ---
    @Nested
    @DisplayName("Get Genders Paginated")
    class GetGendersPaginatedTests {
        @Test
        @DisplayName("Should return a paginated response of GenderDTOs")
        void getGendersPaginated_shouldReturnPagedResponse() {
            int page = 0;
            int size = 5;
            long totalCount = 1L;
            List<Gender> genderList = Collections.singletonList(gender);
            List<GenderDTO> dtoList = Collections.singletonList(genderDTO);
            PanacheQuery<Gender> queryMock = mock(PanacheQuery.class);
            PanacheQuery<Gender> pagedQueryMock = mock(PanacheQuery.class);
            when(genderRepository.findAll()).thenReturn(queryMock);
            when(queryMock.page(page, size)).thenReturn(pagedQueryMock);
            when(pagedQueryMock.list()).thenReturn(Uni.createFrom().item(genderList));
            when(queryMock.count()).thenReturn(Uni.createFrom().item(totalCount));
            when(genderMapper.toDTOList(genderList)).thenReturn(dtoList);

            PagedResponseDTO<GenderDTO> result = genderService.getGendersPaginated(page, size).await().indefinitely();

            assertNotNull(result);
            assertEquals(dtoList, result.getContent());
            assertEquals(1, result.getTotalPages());
        }
    }*/
}