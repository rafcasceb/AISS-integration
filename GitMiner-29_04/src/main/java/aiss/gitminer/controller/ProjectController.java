package aiss.gitminer.controller;

import aiss.gitminer.exceptions.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Tag(name="Project", description="Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository repository;


    @Operation(
            summary = "Retrieve all Projects",
            description = "Get all projects in the database",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects found",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(schema = @Schema())})
            })
    @GetMapping
    public List<Project> findAll(
                                 @RequestParam(value = "title",required = false) String title,
                                 @RequestParam(value = "complexLast",required = false) Boolean complexLast,
                                 @Parameter(description = "page number") @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(value = "order", required = false) String order,
                                 @Parameter(description = "number of elements per page") @RequestParam(defaultValue = "10") int size) {

        Page<Project> pageProjects;
        Pageable paging;
        List<Project> projects = new ArrayList<>();

        if (order != null){
            if(order.startsWith("-"))
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else{
            paging = PageRequest.of(page, size);
        }

        if(title != null){
            projects = repository.findByname(title,paging).getContent();
        }
        else if(complexLast != null){
            if(complexLast){
                if (projects.isEmpty()) projects = repository.findAll(paging).getContent();
                projects = projects.stream().sorted(Comparator.comparing( x -> sizeOfList(x.getCommits())
                                + sizeOfList(x.getIssues()))).toList();
            }
            else{
                pageProjects = repository.findAll(paging);
                projects = pageProjects.getContent();
            }
        }
        else{
            pageProjects = repository.findAll(paging);
            projects = pageProjects.getContent();
        }
        return projects;
    }

    public int sizeOfList(List<?> ls){
        if (ls == null){
            return 0;
        }
        else {
            return ls.size();
        }
    }


    @Operation(
            summary = "Retrieve one project",
            description = "Get one project of the database",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Project findOne(@Parameter(description="Id of the project to find") @PathVariable String id)
            throws ProjectNotFoundException {

        Optional<Project> project = repository.findById(id);

        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }


    @Operation(
            summary = "Create a project",
            description = "Create a project with all the data",
            tags = { "project", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created and returned", content={}),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project project (@Valid @Parameter(description = "Project to be created") @RequestBody Project project){
        // System.out.println(project);
        Project _project = repository
                .save(new Project(project.getId(),
                        project.getName(),
                        project.getWebUrl(),
                        project.getCommits(),
                        project.getIssues()));
        return _project;
    }


    @Operation(
            summary = "Update a project",
            description = "Update a project with all the data",
            tags = { "projects", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated", content = {}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(schema = @Schema())})
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @Parameter(description = "Updated data for project") @RequestBody Project updated,
                       @Parameter(description="Id of the project to be updated") @PathVariable String id)
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


    @Operation(
            summary = "Delete a project",
            description = "Delete a project with all the data",
            tags = { "project", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project deleted", content={}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", description = "Not found", content = { @Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description="Id of the project to be deleted") @PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
