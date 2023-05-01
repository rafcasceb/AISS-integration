package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommentNotFoundException;
import aiss.gitminer.exceptions.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/gitminer/commits")

public class CommitController {
    @Autowired
    CommitRepository repository;

    @GetMapping
    public List<Commit> findCommits
            (@RequestParam(value = "email", required = false) String email)
            throws CommitNotFoundException {

        List<Commit> commits;
        if (email != null) {
            commits = repository.findByauthorEmail(email);
        } else {
            commits = repository.findAll();
        }
        return commits;
    }

    @GetMapping("/{id}")
    public Commit findOne(@PathVariable String id)
            throws  CommitNotFoundException{

        Optional<Commit> commit = repository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Commit create (@Valid @RequestBody Commit commit) {
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Commit updated, @PathVariable String id)
        throws CommitNotFoundException{
        Optional<Commit> commitData = repository.findById(id);

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

}
