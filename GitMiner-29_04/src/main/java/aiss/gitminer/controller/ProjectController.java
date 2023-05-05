package aiss.gitminer.controller;

import aiss.gitminer.exceptions.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {
    @Autowired
    ProjectRepository repository;

    @GetMapping
    public List<Project> findAll(@RequestParam(value = "title",required = false) String title,
                                 @RequestParam(value = "complexLast",required = false) Boolean complexLast,
                                 @RequestParam(defaultValue = "10")int page,
                                 @RequestParam(defaultValue = "10")int size) {

        Page<Project> pageProjects;
        Pageable paging = PageRequest.of(page,size);
        pageProjects = repository.findAll(paging);
        List<Project> projects = pageProjects.getContent();
        if(title != null) projects = projects.stream().filter(x -> x.getName().equals(title)).toList();
        if(complexLast != null){
            if(complexLast){
                projects.sort(Comparator.comparing(x -> x.getCommits().size()
                        + x.getIssues().size() +
                        x.getIssues().stream().mapToInt(y -> y.getComments().size()).sum()));
            }
        }
        return projects;
    }

    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id)
            throws ProjectNotFoundException {

        Optional<Project> project = repository.findById(id);

        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
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
    public void update(@Valid @RequestBody Project updated,@PathVariable String id)
        throws ProjectNotFoundException {

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
