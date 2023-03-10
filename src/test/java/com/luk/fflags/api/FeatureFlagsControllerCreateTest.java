package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FeatureFlagFacade;
import com.luk.fflags.infrastructure.configuration.security.ImportedUsersTestSamples;
import com.luk.fflags.infrastructure.configuration.security.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
@Import({SecurityConfiguration.class, ImportedUsersTestSamples.class})
class FeatureFlagsControllerCreateTest {

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
    void should_return_401_when_username_is_not_found() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("bad_user", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "feature1",
                                "description": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void should_return_401_when_password_is_wrong() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "bad_password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "feature1",
                                "description": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    void should_return_403_when_role_doesnt_match() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("luk", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "feature1",
                                "description": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void should_return_201_when_flag_is_created() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "feature1",
                                "description": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("feature1"))
                .andExpect(MockMvcResultMatchers.jsonPath("description").value("hahaha"));

    }

    @Test
    void should_return_400_name_is_missing() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "description": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void should_return_400_description_is_missing() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "hahaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_400_description_is_empty() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "hahaha",
                                "description": ""
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void should_return_400_name_is_empty() throws Exception {
        mvc.perform(post("/fflags").with(httpBasic("admin", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "",
                                "description": "hsaha"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }




}