package com.luk.fflags.domain.fflags;

import java.util.Optional;

public interface FlagAssigneeRepository {
    Optional<FlagAssignee> findByUsername(String username);
    FlagAssignee save(FlagAssignee flagAssignee);
}
