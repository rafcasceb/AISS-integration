package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.projectsModels.Project;
import aiss.GitHubMiner.services.ProjectService;
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

    String token = "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";

    @Test
    @DisplayName("Get project")
    void getProject(){
        Project project = service.getProject("microsoft","HealthVault-Mobile-iOS-Library", token);
        assertEquals(project.getName(), "HealthVault-Mobile-iOS-Library", "The project doesn't exist.");
    }

    @Test
    @DisplayName("Get all projects")
    void getAllProjects() {
        List<Project> projects = service.getAllProjects("microsoft", token);
        assertEquals(projects.get(0).getId() ,1932083, "The project doesn't exist.");
    }

    @Test
    @DisplayName("Get projects by pages")
    void getProjectsPagination() {
        List<Project> projects = service.getProjectsPagination("microsoft", 1, token);
        assertEquals(projects.size(),20, "The pagination doesn't work.");
    }

}