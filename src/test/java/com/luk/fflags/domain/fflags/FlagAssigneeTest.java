package com.luk.fflags.domain.fflags;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FlagAssigneeTest {


    @Test
    void should_return_empty_flag_set() {
        // given
        FlagAssignee assignee = FlagAssignee.empty("user");

        // when
        Set<Flag> assignedFlags = assignee.getAssignedFlags();

        // then
        Assertions.assertTrue(assignedFlags.isEmpty());
    }

    @Test
    void should_add_flag() {
        // given
        FlagAssignee assignee = FlagAssignee.empty("user");

        // when
        assignee.assignFeatureFlag(Flag.builder()
                .name("name")
                .description("ahaha")
                .globallyEnabled(false)
                .build());

        // then
        assertEquals(1, assignee.getAssignedFlags().size());
    }

    @Test
    void should_remove_flag() {
        // given
        FlagAssignee assignee = FlagAssignee.empty("user");
        assignee.assignFeatureFlag(Flag.builder()
                .name("name")
                .description("ahaha")
                .globallyEnabled(false)
                .build());

        // when
        assignee.removeFlagByName("name");

        // then
        assertEquals(0, assignee.getAssignedFlags().size());
    }


}