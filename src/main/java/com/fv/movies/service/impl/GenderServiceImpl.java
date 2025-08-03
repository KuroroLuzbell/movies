package com.fv.movies.service.impl;

import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.dto.PagedResponseDTO;
import com.fv.movies.entity.Gender;
import com.fv.movies.mapper.IGenderMapper;
import com.fv.movies.repository.impl.GenderRepository;
import com.fv.movies.service.IGenderService;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class GenderServiceImpl implements IGenderService {
    public GenderRepository genderRepository;
    public  IGenderMapper genderMapper;

    @Inject
    public GenderServiceImpl(GenderRepository genderRepository,IGenderMapper genderMapper) {
        this.genderRepository = genderRepository;
        this.genderMapper = genderMapper;
    }

    public GenderServiceImpl() {

    }

    @Override
    @WithSession
    public Uni<List<GenderDTO>> getAllGenders() {
        // 1. Llama al método reactivo del repositorio.
        // 2. Usa 'transform' para convertir la lista de entidades a una lista de DTOs.
        return genderRepository.listAll()
                .onItem().transform(genderMapper::toDTOList);
    }

    @Override
    @WithSession
    public Uni<GenderDTO> getGenderById(Long id) {
        return genderRepository.findById(id)
                // Si el item es nulo (no encontrado), lanza una excepción.
                .onItem().ifNull().failWith(() -> new NotFoundException("Gender with id " + id + " not found."))
                // Si se encuentra, transforma la entidad a DTO.
                .onItem().transform(genderMapper::toDTO);
    }

    @Override
    @WithTransaction
    public Uni<GenderDTO> createGender(GenderDTO genderDTO) {
        Gender gender = genderMapper.toEntity(genderDTO);
        // 'persistAndFlush' guarda la entidad y devuelve un Uni con la entidad persistida.
        return genderRepository.persistAndFlush(gender)
                .onItem().transform(genderMapper::toDTO);
    }

    @Override
    @WithTransaction
    public Uni<GenderDTO> updateGender(Long id, GenderDTO genderDTO) {
        // 'chain' permite encadenar operaciones asíncronas.
        return genderRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException("Gender with id " + id + " not found."))
                .onItem().transform(existingGender -> {
                    // Actualiza la entidad en memoria
                    genderMapper.updateEntityFromDto(genderDTO, existingGender);
                    return existingGender;
                })
                // No es necesario llamar a persist de nuevo, ya que la entidad está gestionada
                // por la sesión de Hibernate Reactivo dentro de la transacción.
                .onItem().transform(genderMapper::toDTO);
    }

    @Override
    @WithTransaction
    public Uni<Void> deleteGender(Long id) {
        return genderRepository.deleteById(id)
                .onItem().transform(deleted -> {
                    if (!deleted) {
                        throw new NotFoundException("Gender with id " + id + " not found, cannot delete.");
                    }
                    return null; // Devuelve null para un Uni<Void>
                });
    }

    @Override
    @WithSession // <-- Anotación para operaciones de solo lectura
    public Uni<PagedResponseDTO<GenderDTO>> getGendersPaginated(int page, int size) {
        // 1. Crear la consulta base sin ejecutarla todavía.
        PanacheQuery<Gender> allGendersQuery = genderRepository.findAll();

        // 2. Crear dos "Unis" a partir de la misma consulta:
        //    - Uno para obtener la lista de la página específica.
        //    - Otro para obtener el conteo total de elementos.
        Uni<List<Gender>> pagedListUni = allGendersQuery.page(page, size).list();
        Uni<Long> totalCountUni = allGendersQuery.count();

        // 3. Combinar los resultados de ambos Unis para construir la respuesta.
        return Uni.combine().all().unis(pagedListUni, totalCountUni).asTuple()
                .onItem().transform(tuple -> {
                    List<Gender> pagedGenders = tuple.getItem1();
                    long totalCount = tuple.getItem2();

                    List<GenderDTO> dtoList = genderMapper.toDTOList(pagedGenders);

                    // Calcular el número total de páginas
                    int totalPages = (totalCount > 0) ? (int) Math.ceil((double) totalCount / size) : 0;

                    // Construir y devolver el DTO de respuesta paginada
                    return new PagedResponseDTO<>(dtoList, page, size, totalCount, totalPages);
                });
    }
}
