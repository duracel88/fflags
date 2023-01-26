package com.luk.fflags.infrastructure.adapters.domain.fflags;

import com.luk.fflags.domain.fflags.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryFlagsRepository implements FlagRepository {
    private final Map<String, Flag> flags = new ConcurrentHashMap<>();

    @Override
    public Optional<Flag> findByName(String name) {
        return Optional.ofNullable(flags.get(name));
    }

    @Override
    public Set<Flag> findAllGloballyEnabled() {
        return flags.values().stream()
                .filter(Flag::isGloballyEnabled)
                .collect(Collectors.toSet());
    }

    @Override
    public Flag save(Flag flag) {
        flags.put(flag.getName(), flag);
        return flag;
    }
}
