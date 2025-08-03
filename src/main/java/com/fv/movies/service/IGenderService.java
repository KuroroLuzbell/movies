package com.fv.movies.service;

import com.fv.movies.dto.request.GenderDTO;
import com.fv.movies.dto.PagedResponseDTO;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface IGenderService {
    Uni<List<GenderDTO>> getAllGenders();

    Uni<GenderDTO> getGenderById(Long id);

    Uni<GenderDTO> createGender(GenderDTO genderDTO);

    Uni<GenderDTO> updateGender(Long id, GenderDTO genderDTO);

    Uni<Void> deleteGender(Long id);

    Uni<PagedResponseDTO<GenderDTO>> getGendersPaginated(int page, int size);

}
