package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {
    @Autowired
    CommentRepository repository;

    @GetMapping
    public List<Comment> findAll(@RequestParam(value = "word", required = false) String word,
                                 @RequestParam(value = "author", required = false) String author,
                                 @RequestParam(value = "recentFirst", required = false) Boolean recentFirst) {

        List<Comment> comments = repository.findAll();

        if (word != null) comments = comments.stream()
                .filter(x -> x.getBody().contains(word)).toList();
        if (author != null) comments = comments.stream()
                .filter(x -> x.getAuthor().getUsername().equals(author)).toList();

        if (recentFirst != null){
            if (recentFirst) {
                comments.sort(Comparator.comparing(x -> ZonedDateTime.parse(x.getUpdatedAt()).toLocalDate()));
                //reversed
            } else {
                comments.sort(Comparator.comparing(x -> ZonedDateTime.parse(x.getUpdatedAt()).toLocalDate()));
            }
        }


        return comments;

    }

    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws  CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
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
    public void update(@Valid @RequestBody Comment updated,@PathVariable String id)
        throws CommentNotFoundException {

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
    public void delete (@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
