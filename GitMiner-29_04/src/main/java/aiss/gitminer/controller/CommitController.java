package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommentNotFoundException;
import aiss.gitminer.exceptions.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name="Comment", description = "Comment management APi")
@RestController
@RequestMapping("/gitminer/commits")

public class CommitController {
    @Autowired
    CommitRepository repository;

    @Operation(summary = "Retrieve all commits", description = "Get al commits", tags = {"commits","get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class), mediaType =  "application/json")}),
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping
    public List<Commit> findCommits
            (@Parameter(description = "email of the author of the comments to find") @RequestParam(value = "email", required = false) String email,
             @Parameter(description = "committer of the comments to find") @RequestParam(value = "committername", required = false) String committerName,
             @Parameter(description = "limit date of the comments to be find") @RequestParam(value = "beforedate", required = false) String beforeDate,
             @Parameter(description = "page number") @RequestParam(defaultValue="0")int page,
             @Parameter(description = "number of elements per page") @RequestParam(defaultValue = "10")int size)
    {


        Page<Commit> pageCommit;
        Pageable paging = PageRequest.of(page,size);
        pageCommit = repository.findAll(paging);
        List<Commit> commits = pageCommit.getContent();

        if (email != null) commits = commits.stream()
                .filter(x -> repository.findByauthorEmail(email).contains(x)).toList();
        if (committerName != null) commits = commits.stream()
                .filter(x -> x.getCommitterName().replace(" ","").equals(committerName)).toList();
        if (beforeDate != null) commits = commits.stream()
                .filter(x -> ZonedDateTime.parse(x.getCommittedDate()).toLocalDate().isBefore(ZonedDateTime.parse(beforeDate).toLocalDate())).toList();

        return commits;
    }

    @Operation(summary = "Retrieve a commit by id", description = "Get a commit by its identifier", tags = {"commits","get","id"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class), mediaType =  "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public Commit findOne(@Parameter(description = "Id of the commit to find")@PathVariable String id)
            throws  CommitNotFoundException{

        Optional<Commit> commit = repository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

    @Operation(summary = "Upload a commit", description = "Post operation to save a commit in the database", tags = {"commits","post"})
    @ApiResponses({@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Commit.class), mediaType =  "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Commit create (@Valid @Parameter(description = "The commit to post") @RequestBody Commit commit) {
        Commit _commit = repository
                .save(new Commit(commit.getId(),
                        commit.getTitle(),
                        commit.getMessage(),
                        commit.getAuthorName(),
                        commit.getAuthorEmail(),
                        commit.getAuthoredDate(),
                        commit.getCommitterName(),
                        commit.getCommitterEmail(),
                        commit.getCommittedDate(),
                        commit.getWebUrl()));
        return _commit;
    }

    @Operation(summary = "Update a commit", description = "Put operation to update the commit with the given id", tags = {"commits","put", "id"})
    @ApiResponses({@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @Parameter(description = "Commit data to be updated") @RequestBody Commit updated, @Parameter(description = "Id of the comment to be updated") @PathVariable String id)
        throws CommitNotFoundException{

        Optional<Commit> commitData = repository.findById(id);
        if(!commitData.isPresent()){
            throw new CommitNotFoundException();
        }

        Commit _commit = commitData.get();
        _commit.setId(updated.getId());
        _commit.setTitle(updated.getTitle());
        _commit.setAuthorName(updated.getAuthorName());
        _commit.setAuthorEmail(updated.getAuthorEmail());
        _commit.setAuthoredDate(updated.getAuthoredDate());
        _commit.setCommitterName(updated.getCommitterName());
        _commit.setCommitterEmail(updated.getCommitterEmail());
        _commit.setCommittedDate(updated.getCommittedDate());
        _commit.setWebUrl(updated.getWebUrl());

        repository.save(_commit);
    }

    @Operation(summary = "Update a commit", description = "Put operation to update the commit with the given id", tags = {"commits","put", "id"})
    @ApiResponses({@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

}
