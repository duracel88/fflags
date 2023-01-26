package com.luk.fflags.domain.fflags;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlagServiceTest {

    @Mock
    FlagRepository flagRepository;

    @InjectMocks
    FlagService service;

    @Test
    void new_created_flag_is_by_default_disabled(){
        service.createFeatureFlag("flagName", "Super Feature");

        Mockito.verify(flagRepository).save(Mockito.argThat(flag ->
                flag.getName().equals("flagName") &&
                flag.getDescription().equals("Super Feature") &&
                !flag.isGloballyEnabled()));
    }

    @Test
    void flag_name_cannot_be_null(){
        Assertions.assertThrows(NullPointerException.class, () -> service.createFeatureFlag(null, "Super Feature"));
    }

    @Test
    void flag_desc_cannot_be_null(){
        Assertions.assertThrows(NullPointerException.class, () -> service.createFeatureFlag("id", null));
    }
}