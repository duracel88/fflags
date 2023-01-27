package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FeatureFlagFacade;
import com.luk.fflags.infrastructure.configuration.security.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest
@Import(SecurityConfiguration.class)
class FeatureFlagsControllerDeallocateTest {

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
    void should_return_204_when_user_is_deallocated_from_flag() throws Exception {
        when(flagFacade.removeAssigneeFromFeatureFlag("luk", "feature1"))
                .thenReturn(true);
        mvc.perform(delete("/fflags/feature1/assignee/luk").with(httpBasic("admin", "password")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void should_return_403_when_authenticated_user_is_not_authorized() throws Exception {
        when(flagFacade.removeAssigneeFromFeatureFlag("luk", "feature1"))
                .thenReturn(true);
        mvc.perform(delete("/fflags/feature1/assignee/luk").with(httpBasic("luk", "password")))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void should_return_404_feature_flag_or_assignee_doesnt_exist() throws Exception {
        when(flagFacade.removeAssigneeFromFeatureFlag("luk", "feature1"))
                .thenReturn(false);
        mvc.perform(delete("/fflags/feature1/assignee/luk").with(httpBasic("admin", "password")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}