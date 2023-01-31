package com.luk.fflags.domain.fflags;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlagService {
    private final FlagRepository repository;

    Flag createFeatureFlag(@NonNull String name, @NonNull String description) {
        Flag flag = Flag.builder()
                .name(name)
                .description(description)
                .globallyEnabled(false)
                .build();
        return repository.save(flag);
    }

}
