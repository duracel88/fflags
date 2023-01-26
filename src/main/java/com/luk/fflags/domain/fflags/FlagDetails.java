package com.luk.fflags.domain.fflags;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class FlagDetails {
    @NonNull String name;
    @NonNull String description;
    @NonNull boolean isGloballyEnabled;
}
