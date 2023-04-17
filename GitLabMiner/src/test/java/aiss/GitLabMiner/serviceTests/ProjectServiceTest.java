package aiss.GitLabMiner.serviceTests;

import aiss.GitLabMiner.Models.Projects.Project;
import aiss.GitLabMiner.Service.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProjectServiceTest {
    @Autowired
    ProjectService service;

    String token = "glpat-JqffjHbjrCBtJZLNPENh";

    @Test
    @DisplayName("Get project test")
    void getAllProjects(){
        List<Project> listProjects = service.getAllProjects(token);
        System.out.println(listProjects.get(0).getId());
    }
    @Test
    @DisplayName("Get project test")
    void getProject(){
        Project project = service.getProject("45236382", token);
        System.out.println(project);
    }
}