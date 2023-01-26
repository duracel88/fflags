package com.luk.fflags.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FeatureFlagDto {
    @NotNull(message = "name must not be null")
    @NotEmpty(message = "name must not be empty")
    String name;

    @NotNull(message = "description must not be null")
    @NotEmpty(message = "description must not be empty")
    String description;

    Boolean globallyEnabled;
}
