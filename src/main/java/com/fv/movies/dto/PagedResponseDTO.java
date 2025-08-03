package com.fv.movies.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Un DTO genérico para estandarizar las respuestas paginadas de la API.
 * @param <T> El tipo del contenido en la página (ej. GenderDTO, MovieDTO).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDTO<T> {
    /** La lista de elementos para la página actual. */
    private List<T> content;

    /** El número de la página actual (basado en 0). */
    private int currentPage;

    /** El tamaño de la página solicitado. */
    private int pageSize;

    /** El número total de elementos en todas las páginas. */
    private long totalElements;

    /** El número total de páginas disponibles. */
    private int totalPages;
}
