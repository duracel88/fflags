package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FeatureFlagFacade;
import com.luk.fflags.infrastructure.configuration.security.ImportedUsersTestSamples;
import com.luk.fflags.infrastructure.configuration.security.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
@Import({SecurityConfiguration.class, ImportedUsersTestSamples.class})
class FeatureFlagsControllerGetMyGlobalTest {

    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    FeatureFlagFacade flagFacade;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void should_return_200_with_mix_of_mine_and_global_enabled() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("my", "true").param("global", "true")
                        .with(httpBasic("luk", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void should_return_201_username_is_not_found() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("my", "true").param("global", "true")
                        .with(httpBasic("bad_user", "password")))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void should_return_201_password_is_bad() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("my", "true").param("global", "true")
                        .with(httpBasic("luk", "xxxx")))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void should_return_400_when_global_true_param_is_missing() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("my", "true")
                        .with(httpBasic("luk", "password")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_400_when_my_true_param_is_missing() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("global", "true")
                        .with(httpBasic("luk", "password")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_403_when_authenticated_user_is_not_authorized() throws Exception {
        Mockito.when(flagFacade.getAllFlagsForUser("luk"))
                .thenReturn(List.of());
        mvc.perform(get("/fflags").param("global", "true").param("my", "true")
                        .with(httpBasic("other_user", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}