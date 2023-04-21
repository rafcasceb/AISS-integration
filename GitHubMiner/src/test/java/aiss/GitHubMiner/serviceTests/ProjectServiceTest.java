package aiss.GitHubMiner.serviceTests;

import aiss.GitHubMiner.models.commitsModels.Commit;
import aiss.GitHubMiner.models.projectsModels.Projects;
import aiss.GitHubMiner.services.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    String token = "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";

    @Test
    @DisplayName("Get project")
    void getProject(){
        Projects project = service.getProject("3525357", token);
        assertTrue(project.getId() == 3525357, "The project doesn't exist");
        System.out.println(project.getId());
    }

    @Test
    @DisplayName("Get all projects")
    void getAllProjects() {
        List<Projects> projects = service.getAllProjects("monicahq", "monica", token);
        assertTrue(!projects.isEmpty(), "The list of projects is empty!");
        System.out.println(projects);
        }

    @Test
    @DisplayName("Get organization projects")
    void getOrgProjects(){
        List<Projects> listProjects = service.getOrgProjects("microsoft");
        assertTrue(!listProjects.isEmpty(), "The list of projects for this organization is empty!");
        System.out.println(listProjects);
    }

}