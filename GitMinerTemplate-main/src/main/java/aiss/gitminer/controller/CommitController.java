package aiss.gitminer.controller;

import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/commits")

public class CommitController {
    @Autowired
    CommitRepository repository;

    @GetMapping
    public List<Commit> findAll (){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Commit findOne(@PathVariable long id){
        Optional<Commit> commit = repository.findById(id);
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
    public void update(@Valid @RequestBody Commit updated, @PathVariable long id){
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
    public void delete (@PathVariable long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

}
