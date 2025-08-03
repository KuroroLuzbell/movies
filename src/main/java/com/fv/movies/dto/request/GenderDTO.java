package com.fv.movies.dto.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@RegisterForReflection
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "description cannot be null")
    @NotBlank(message = "description cannot be blank")
    private String description;

    private LocalDateTime createAt = LocalDateTime.of(1990, 1, 1, 0,0,0);


}
