package com.luk.fflags.domain.fflags;

import java.util.Optional;
import java.util.Set;

public interface FlagRepository {
    Optional<Flag> findByName(String name);
    Set<Flag> findAllGloballyEnabled();
    Flag save(Flag flag);
}
