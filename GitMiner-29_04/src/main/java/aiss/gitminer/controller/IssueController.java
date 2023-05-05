package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommitNotFoundException;
import aiss.gitminer.exceptions.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {
    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> findIssues
            (@RequestParam(value = "authorId",required = false) String id,
             @RequestParam(value = "state",required = false)String state,
             @RequestParam(value = "keyword",required = false) String keyword,
             @RequestParam(value = "longerFirst", required = false) Boolean longerFirst,
             @RequestParam(defaultValue = "0")int page,
             @RequestParam(defaultValue = "10")int size)
            throws IssueNotFoundException {


        Page<Issue> pageIssue;
        Pageable paging = PageRequest.of(page,size);
        pageIssue = repository.findAll(paging);
        List<Issue> issues = pageIssue.getContent();


        if(id != null) issues = filterByAuthorId (id, issues);
        if(state != null) issues = filterByState (state, issues);

        if (keyword != null) issues = issues.stream().filter(x -> x.getLabels().contains(keyword)
                || (x.getDescription() != null && x.getDescription().contains(keyword))
                || x.getTitle().contains(keyword)).toList();

        if (longerFirst != null){
            if (longerFirst) {
                issues.sort(Comparator.comparing(x -> getLenghtDescription(x.getDescription())));
                //reversed
            } else {
                issues.sort(Comparator.comparing(x -> getLenghtDescription(x.getDescription())));
            }
        }

        return issues;
    }

    public Integer getLenghtDescription (String description){
        if (description == null) {
            return 0;
        } else {
            return description.length();
        }
    }

    @GetMapping("/{id}")
    public Issue findOne(@PathVariable String id)
            throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    @GetMapping("/{id}/comments")

    public List<Comment> findCommentsOfOne(@PathVariable String id)
            throws IssueNotFoundException {

        Optional<Issue> issue = repository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get().getComments();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Issue create(@Valid @RequestBody Issue issue){
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Issue updated,@PathVariable String id)
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable String id){
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
