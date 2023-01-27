package com.luk.fflags.domain.fflags;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class FlagAssigneeService {
    private final FlagRepository flagRepository;
    private final FlagAssigneeRepository flagAssigneeRepository;

    void assignFeatureFlag(@NonNull String assigneeUsername, @NonNull String flagName) {
        Flag flag = flagRepository.findByName(flagName).orElseThrow(() -> new FlagNotFoundException("flagName=" + flagName));
        FlagAssignee flagAssignee = flagAssigneeRepository.findByUsername(assigneeUsername)
                .orElseGet(() -> FlagAssignee.empty(assigneeUsername));
        flagAssignee.assignFeatureFlag(flag);
        flagAssigneeRepository.save(flagAssignee);
    }

    Set<Flag> getAllFeatureFlagsForUser(@NonNull String assigneeUsername) {
        Set<Flag> assigned = flagAssigneeRepository.findByUsername(assigneeUsername)
                .map(FlagAssignee::getAssignedFlags)
                .orElseGet(Set::of);
        Set<Flag> allEnabled = flagRepository.findAllGloballyEnabled();
        return concat(assigned.stream(), allEnabled.stream())
                .collect(toSet());
    }

    public boolean removeAssigneeFromFeatureFlag(String assigneeUsername, String flagName) {
        return flagAssigneeRepository.findByUsername(assigneeUsername)
                .map(assignee -> assignee.removeFlagByName(flagName))
                .orElse(false);
    }
}
