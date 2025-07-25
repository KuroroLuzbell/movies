package com.fv.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenderDTO {
    public Integer id;
    public String description;
    public LocalDateTime createAt;
}
