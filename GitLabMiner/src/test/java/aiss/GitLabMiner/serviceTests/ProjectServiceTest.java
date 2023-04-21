package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProjectServiceTest {
    @Autowired
    ProjectService service;

    String token = "glpat-JqffjHbjrCBtJZLNPENh";

    @Test
    @DisplayName("Get all projects test")
    void getAllProjects(){
        List<Project> listProjects = service.getAllProjects(token);
        // It always should return 20 projects
        assertEquals(listProjects.size(), 20, "The id of the project is not correct.");
    }
    @Test
    @DisplayName("Get project test")
    void getProject(){
        Project project = service.getProject("45236382", token);
        assertEquals(project.getId(), 45236382, "The id of the project is not correct.");
    }
}