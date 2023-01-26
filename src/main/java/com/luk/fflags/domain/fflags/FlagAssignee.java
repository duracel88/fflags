package com.luk.fflags.domain.fflags;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

@Value
@Builder
public class FlagAssignee {
    @NonNull String username;
    @NonNull Set<Flag> assignedFlags;

    public static FlagAssignee empty(String username){
        return FlagAssignee.builder()
                .username(username)
                .assignedFlags(new HashSet<>())
                .build();
    }

    void assignFeatureFlag(Flag flag) {
        this.assignedFlags.add(flag);
    }

    void removeFlagByName(String flagName){
        this.assignedFlags.removeIf(flag -> flag.getName().equals(flagName));
    }
}
