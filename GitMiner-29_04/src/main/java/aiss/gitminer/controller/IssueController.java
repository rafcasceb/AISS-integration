package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommitNotFoundException;
import aiss.gitminer.exceptions.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Tag(name="Issue",description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository repository;


    @Operation(
            summary = "Retrieve a list of issues",
            description = "Get a list of issues",
            tags = { "issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())})
    })
    @GetMapping
    public List<Issue> findIssues
            (@Parameter(description = "Id of the author")@RequestParam(value = "authorId",required = false) String id,
             @Parameter(description = "State of the issue")@RequestParam(value = "state",required = false)String state,
             @Parameter(description = "Keyword to filter")@RequestParam(value = "keyword",required = false) String keyword,
             @Parameter(description = "Order by long first")@RequestParam(value = "longerFirst", required = false) Boolean longerFirst,
             @Parameter(description = "Page retrieved")@RequestParam(defaultValue = "0")int page,
             @RequestParam(value = "order", required = false) String order,
             @Parameter(description = "Number of elements retrieved")@RequestParam(defaultValue = "10")int size)
            throws IssueNotFoundException {


        Pageable paging;
        Page<Issue> pageIssue;
        List<Issue> issues;
        if (order != null){
            if(order.startsWith("-"))
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else{
            paging = PageRequest.of(page, size);
        }


        if(id != null){
            pageIssue = repository.findAll(paging);
            issues = pageIssue.getContent();
            issues = filterByAuthorId (id, issues);
        }
        else if(state != null) {
            pageIssue = repository.findAll(paging);
            issues = pageIssue.getContent();
            issues = filterByState (state, issues);
        }
        else if (keyword != null){
            pageIssue = repository.findAll(paging);
            issues = pageIssue.getContent();
            issues = issues.stream().filter(x -> x.getLabels().contains(keyword)
                    || (x.getDescription() != null && x.getDescription().contains(keyword))
                    || x.getTitle().contains(keyword)).toList();
        }else{
            pageIssue = repository.findAll(paging);
            issues = pageIssue.getContent();
        }




        return issues;
    }

    private Integer getLenghtDescription (String description){
        if (description == null) {
            return 0;
        } else {
            return description.length();
        }
    }


    @Operation(
            summary = "Retrieve an issue",
            description = "Get an issue",
            tags = { "issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Issue findOne(@Parameter(description = "Id of the issue")@PathVariable String id)
            throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }


    @Operation(
            summary = "Retrieve comments of an issue",
            description = "Get comments of an issue",
            tags = { "issue", "get", "comments"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsOfOne(@Parameter(description = "Id of the issue")@PathVariable String id)
            throws IssueNotFoundException {

        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get().getComments();
    }


    @Operation(
            summary = "Create an issue",
            description = "Create an issue whose data is passed in the body of the request",
            tags = { "issue", "create"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Issue create(@Parameter(description = "Issue to be created")@Valid @RequestBody Issue issue){
        Issue _issue = repository
                .save(new Issue(issue.getId(),
                        issue.getRefId(),
                        issue.getTitle(),
                        issue.getDescription(),
                        issue.getState(),
                        issue.getCreatedAt(),
                        issue.getUpdatedAt(),
                        issue.getClosedAt(),
                        issue.getLabels(),
                        issue.getAuthor(),
                        issue.getAssignee(),
                        issue.getUpvotes(),
                        issue.getDownvotes(),
                        issue.getWebUrl(),
                        issue.getComments()
                        ));
        return _issue;
    }


    @Operation(
            summary = "Update an issue",
            description = "Update an existing issue with data passed on the body of the request",
            tags = { "issue", "update"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}),
                    @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Parameter(description = "Issue information to be updated")@Valid @RequestBody Issue updated,
                       @Parameter(description = "Id of the issue to update")@PathVariable String id)
            throws IssueNotFoundException{

        Optional<Issue> issueData = repository.findById(id);

        Issue _issue = issueData.get();
        _issue.setId(updated.getId());
        _issue.setRefId(updated.getRefId());
        _issue.setTitle(updated.getTitle());
        _issue.setDescription(updated.getDescription());
        _issue.setState(updated.getState());
        _issue.setCreatedAt(updated.getCreatedAt());
        _issue.setUpdatedAt((updated.getUpdatedAt()));
        _issue.setClosedAt(updated.getClosedAt());
        _issue.setLabels(updated.getLabels());
        _issue.setAuthor(updated.getAuthor());
        _issue.setAssignee(updated.getAssignee());
        _issue.setUpvotes(updated.getUpvotes());
        _issue.setWebUrl(updated.getWebUrl());
        _issue.setComments(updated.getComments());

        repository.save(_issue);
    }


    @Operation(
            summary = "Delete an issue",
            description = "Delete an existing issue by the id specified",
            tags = { "issue", "delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",content = {@Content(schema = @Schema(implementation = Issue.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400",content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@Parameter(description = "Issue id to be deleted")@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

    List<Issue> filterByAuthorId (String id, List<Issue> Issues){

        List<Issue> newIssues = new ArrayList<>(Issues);

        for(Issue issue : Issues){
            if (!issue.getAuthor().getId().equals(id)) {
                System.out.println(issue.getAuthor().getId() + " " + id + "-------------------------------------------------------------------------------------------------------------------------------------");
                newIssues.remove(issue);
            }
        }
        return  newIssues;
    }

    List<Issue> filterByState (String state, List<Issue> Issues){

        List<Issue> issueByState = repository.findByState(state);
        List<Issue> newIssues = new ArrayList<>(Issues);

        for(Issue issue : Issues){
            if (!issueByState.contains(issue)) {
                newIssues.remove(issue);
            }
        }
        return  newIssues;
    }
}
