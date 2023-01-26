package com.luk.fflags.domain.fflags;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class Flag {

    @NonNull
    private String name;

    @NonNull
    private String description;

    boolean globallyEnabled;

    void toggle() {
        globallyEnabled = !globallyEnabled;
    }

}
