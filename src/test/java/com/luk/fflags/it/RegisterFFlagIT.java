package com.luk.fflags.it;

import com.luk.fflags.domain.fflags.FlagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterFFlagIT {

    @Autowired
    FlagRepository flagRepository;

    @Autowired
    MockMvc mvc;

    @Test
    void assignmentIntegrationTest() throws Exception {
        // given flag f1 is created
        MockHttpServletRequestBuilder createRequest = post("/fflags")
                .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "f1",
                            "description": "d1"
                        }
                        """);
        mvc.perform(createRequest);

        // and assigned to luk
        MockHttpServletRequestBuilder assignRequest = post("/fflags/f1/assignee")
                .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "assigneeUsername": "luk"
                        }
                        """);

        MockHttpServletRequestBuilder getLuksAndGlobal = get("/fflags")
                .param("global", "true")
                .param("my", "true")
                .with(httpBasic("luk", "password"));

        // when luk gets his and global flags
        mvc.perform(assignRequest);
        ResultActions getResult = mvc.perform(getLuksAndGlobal);

        // then he gets one result
        getResult
                .andExpect(jsonPath("$.[?(@.name == 'f1')]", jsonPath("$.description", is("d1"))).exists())
                .andExpect(jsonPath("$.[?(@.name == 'f2')]", jsonPath("$.description", is("d1"))).doesNotExist());

        // and then new f2 flag is created
        MockHttpServletRequestBuilder newFlagCreateRequest = post("/fflags")
                .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "f2",
                            "description": "d1"
                        }
                        """);
        mvc.perform(newFlagCreateRequest);

        // and assigned to different user
        MockHttpServletRequestBuilder adminAssignment = post("/fflags/f2/assignee")
                .with(httpBasic("admin", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "assigneeUsername": "admin"
                        }
                        """);
        mvc.perform(adminAssignment);

        MockHttpServletRequestBuilder getLuksAndGlobalAgain = get("/fflags")
                .param("global", "true")
                .param("my", "true")
                .with(httpBasic("luk", "password"));


        // then f2 still doesn't exist in luk's result set
        mvc.perform(getLuksAndGlobalAgain)
                .andExpect(jsonPath("$.[?(@.name == 'f1')]", jsonPath("$.description", is("d1"))).exists())
                .andExpect(jsonPath("$.[?(@.name == 'f2')]", jsonPath("$.description", is("d1"))).doesNotExist());
    }


}
