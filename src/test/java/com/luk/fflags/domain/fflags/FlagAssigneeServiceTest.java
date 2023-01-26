package com.luk.fflags.domain.fflags;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlagAssigneeServiceTest {

    @Mock
    FlagRepository flagRepository;

    @Mock
    FlagAssigneeRepository flagAssigneeRepository;

    @InjectMocks
    FlagAssigneeService service;

    @Test
    void should_assign_flag_to_username() {
        Mockito.when(flagRepository.findByName("flagName")).thenReturn(Optional.of(Flag.builder()
                .name("flagName")
                .description("desc")
                .globallyEnabled(false)
                .build()));

        Mockito.when(flagAssigneeRepository.findByUsername("username"))
                .thenReturn(Optional.of(FlagAssignee.empty("username")));

        service.assignFeatureFlag("username", "flagName");

        Mockito.verify(flagAssigneeRepository).save(Mockito.argThat(flagAssignee -> flagAssignee.getAssignedFlags().size() == 1));
    }

    @Test
    void should_assign_flag_to_username_even_when_its_already_assigned_to_different_flag() {
        Mockito.when(flagRepository.findByName("flagName")).thenReturn(Optional.of(Flag.builder()
                .name("flagName")
                .description("desc")
                .globallyEnabled(false)
                .build()));

        Set<Flag> flags = new HashSet<Flag>();
        flags.add(Flag.builder()
                .globallyEnabled(true)
                .description("desc")
                .name("differentName")
                .build());

        Mockito.when(flagAssigneeRepository.findByUsername("username"))
                .thenReturn(Optional.of(FlagAssignee.builder()
                        .assignedFlags(flags)
                        .username("username")
                        .build()));

        service.assignFeatureFlag("username", "flagName");

        Mockito.verify(flagAssigneeRepository).save(Mockito.argThat(flagAssignee -> flagAssignee.getAssignedFlags().size() == 2));
    }


    @Test
    void should_return_combination_of_all_global_enabled_and_assigned_to_user_for_the_user() {
        Mockito.when(flagRepository.findAllGloballyEnabled()).thenReturn(Set.of(
                Flag.builder()
                        .name("flagName")
                        .description("desc")
                        .globallyEnabled(true)
                        .build()));

        Set<Flag> flags = new HashSet<Flag>();
        flags.add(Flag.builder()
                .globallyEnabled(true)
                .description("desc")
                .name("differentName")
                .build());

        Mockito.when(flagAssigneeRepository.findByUsername("username"))
                .thenReturn(Optional.of(FlagAssignee.builder()
                        .assignedFlags(flags)
                        .username("username")
                        .build()));

        Set<Flag> allFlags = service.getAllFeatureFlagsForUser("username");

        Assertions.assertTrue(allFlags.stream().anyMatch(flag -> flag.getName().equals("differentName")));
        Assertions.assertTrue(allFlags.stream().anyMatch(flag -> flag.getName().equals("flagName")));
    }

}