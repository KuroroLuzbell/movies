package com.fv.movies.mapper;

import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.entity.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper para convertir entre la entidad Gender y su DTO.
 * componentModel = "cdi" permite que Quarkus lo inyecte donde se necesite.
 */
@Mapper(componentModel = "cdi")
public interface IGenderMapper {

    GenderDTO toDTO(Gender gender);

    @Mapping(target = "id", ignore = true)
    Gender toEntity(GenderDTO genderDTO);

    List<GenderDTO> toDTOList(List<Gender> genders);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(GenderDTO dto, @MappingTarget Gender entity);
}
