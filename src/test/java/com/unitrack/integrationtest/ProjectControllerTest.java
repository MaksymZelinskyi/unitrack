package com.unitrack.integrationtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"classpath:/testdata/collaborator-activity-testdata.sql"})
public class ProjectControllerTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testadmin@email.com", authorities = "ROLE_ADMIN")
    public void testProjectPageIsGeneratedProperly() throws Exception {
        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("project"))
                .andExpect(model().attributeHasNoErrors("project"));
    }
}
