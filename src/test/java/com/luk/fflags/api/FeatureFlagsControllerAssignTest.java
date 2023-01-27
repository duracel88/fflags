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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
@Import({SecurityConfiguration.class, ImportedUsersTestSamples.class})
class FeatureFlagsControllerAssignTest {

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
    void should_return_201_when_flag_is_assigned() throws Exception {
        mvc.perform(post("/fflags/feature1/assignee").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "assigneeUsername": "luk"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(flagFacade).assignFeatureFlagToUser("luk", "feature1");
    }

    @Test
    void should_return_403_when_authenticated_user_is_not_authorized() throws Exception {
        mvc.perform(post("/fflags/feature1/assignee").with(httpBasic("luk", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "assigneeUsername": "luk"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verify(flagFacade, times(0)).assignFeatureFlagToUser("luk", "feature1");
    }

    @Test
    void should_return_400_when_assigneeUsername_field_is_missing() throws Exception {
        mvc.perform(post("/fflags/feature1/assignee").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "other": "luk"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(flagFacade, times(0)).assignFeatureFlagToUser("luk", "feature1");
    }

    @Test
    void should_return_400_when_assigneeUsername_is_empty() throws Exception {
        mvc.perform(post("/fflags/feature1/assignee").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "assigneeUsername": ""
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(flagFacade, times(0)).assignFeatureFlagToUser("luk", "feature1");
    }




}