package com.luk.fflags.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FeatureFlagAssigneeDto {

    @NotNull(message = "assigneeUsername must not be null")
    @NotEmpty(message = "assigneeUsername must not be empty")
    String assigneeUsername;
}
