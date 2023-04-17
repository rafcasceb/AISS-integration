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

    @Test
    @DisplayName("Get project")
    void getProject(){
        Projects project = service.getProject("23",
                "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");
        System.out.println(project);
    }

    @Test
    @DisplayName("Get all projects")
    void getAllProjects() {
        List<Projects> projects = service.getAllProjects("spring-projects", "spring-framework",
                "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");
        assertTrue(!projects.isEmpty(), "The list of projects is empty!");
        System.out.println(projects);
        }

    @Test
    @DisplayName("Get organization projects")
    void getOrgProjects(){
        List<Projects> listProjects = service.getOrgProjects("fjdfi");
        assertTrue(!listProjects.isEmpty(), "The list of projects for this organization is empty!");
        System.out.println(listProjects);
    }

}