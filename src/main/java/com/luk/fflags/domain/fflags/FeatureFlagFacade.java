package com.luk.fflags.domain.fflags;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class FeatureFlagFacade {
    private final FlagService flagService;
    private final FlagAssigneeService flagAssigneeService;

    public FlagDetails createFeatureFlag(@NonNull String flagName, @NonNull String flagDescription) {
        Flag flag = flagService.createFeatureFlag(flagName, flagDescription);
        return FlagDetails.builder()
                .name(flag.getName())
                .description(flag.getDescription())
                .isGloballyEnabled(flag.isGloballyEnabled())
                .build();
    }

    public void assignFeatureFlagToUser(@NonNull String username, @NonNull String flagName) {
        flagAssigneeService.assignFeatureFlag(username, flagName);
    }

    public boolean removeAssigneeFromFeatureFlag(@NonNull String username, @NonNull String flagName) {
        return flagAssigneeService.removeAssigneeFromFeatureFlag(username, flagName);
    }

    public List<FlagDetails> getAllFlagsForUser(@NonNull String username) {
        return flagAssigneeService.getAllFeatureFlagsForUser(username).stream()
                .map(flag -> FlagDetails.builder()
                        .name(flag.getName())
                        .description(flag.getDescription())
                        .build())
                .collect(toList());
    }


}
