package aiss.gitminer.controller;

import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    ProjectRepository repository;

    @GetMapping
    public List<Project> findAll(){return repository.findAll();}

    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id){
        Optional<Project> project = repository.findById(id);
        return project.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project project (@Valid @RequestBody Project project){
        // System.out.println(project);
        Project _project = repository
                .save(new Project(project.getId(),
                        project.getName(),
                        project.getWebUrl(),
                        project.getCommits(),
                        project.getIssues()));
        return _project;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Project updated,@PathVariable String id){
        Optional<Project> projectData = repository.findById(id);

        Project _project = projectData.get();
        _project.setId(updated.getId());
        _project.setName(updated.getName());
        _project.setWebUrl(updated.getWebUrl());
        _project.setCommits(updated.getCommits());
        _project.setIssues(updated.getIssues());
        repository.save(_project);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
