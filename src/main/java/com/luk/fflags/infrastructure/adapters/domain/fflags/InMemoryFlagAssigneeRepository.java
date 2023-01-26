package com.luk.fflags.infrastructure.adapters.domain.fflags;

import com.luk.fflags.domain.fflags.Flag;
import com.luk.fflags.domain.fflags.FlagAssignee;
import com.luk.fflags.domain.fflags.FlagAssigneeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFlagAssigneeRepository implements FlagAssigneeRepository {
    private final Map<String, FlagAssignee> assignedFlags = new ConcurrentHashMap<>();

    @Override
    public Optional<FlagAssignee> findByUsername(String username) {
        return Optional.ofNullable(assignedFlags.get(username));
    }

    @Override
    public FlagAssignee save(FlagAssignee flagAssignee) {
        assignedFlags.put(flagAssignee.getUsername(), flagAssignee);
        return flagAssignee;
    }
}
