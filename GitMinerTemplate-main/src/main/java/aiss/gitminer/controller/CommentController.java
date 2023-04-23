package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.List;
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    CommentRepository repository;

    @GetMapping
    public List<Comment> findAll(){return repository.findAll();}

    @GetMapping("/{id}")
    public Comment findOne(@PathVariable long id){
        Optional<Comment> comment = repository.findById(id);
        return comment.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Comment create (@Valid @RequestBody Comment comment){
        Comment _comment = repository
                .save(new Comment(comment.getId(),
                        comment.getBody(),
                        comment.getAuthor(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()));
        return _comment;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Comment updated,@PathVariable long id){
        Optional<Comment> commentData = repository.findById(id);

        Comment _comment = commentData.get();
        _comment.setId(updated.getId());
        _comment.setBody(updated.getBody());
        _comment.setAuthor(updated.getAuthor());
        _comment.setCreatedAt(updated.getCreatedAt());
        _comment.setUpdatedAt(updated.getUpdatedAt());

        repository.save(_comment);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable long id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
