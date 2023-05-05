package aiss.gitminer.controller;

import aiss.gitminer.exceptions.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@Tag(name = "Comment", description = "Comment controller")
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {
    @Autowired
    CommentRepository repository;


    @Operation(
            summary = "Retrieve all Comments",
            description = "Get all comments in the database",
            tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments list",
                    content = {@Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Not found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public List<Comment> findAll(@RequestParam(value = "word", required = false) String word,
                                 @RequestParam(value = "author", required = false) String author,
                                 @RequestParam(value = "recentFirst", required = false) Boolean recentFirst,
                                 @RequestParam(defaultValue = "0")int page,
                                 @RequestParam(defaultValue = "10")int size) {


        Page<Comment> pageComments;
        Pageable paging = PageRequest.of(page,size);
        pageComments = repository.findAll(paging);
        List<Comment> comments = pageComments.getContent();

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


    @Operation(
            summary = "Retrieve one Comments",
            description = "Get one comments with the id",
            tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment",
                    content = {@Content(schema = @Schema(implementation = Comment.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Not found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws  CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }


    @Operation(
            summary = "Create a comment",
            description = "Create a comment with all the data",
            tags = { "comments", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment", content = {})
    })
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

    @Operation(
            summary = "Update a comment",
            description = "Update a comment with all the data",
            tags = { "comments", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment", content = {}),
            @ApiResponse(responseCode = "500", description = "Not found", content = { @Content(schema = @Schema()) })
    })
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

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment with the id",
            tags = { "comments", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment", content = {})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }
}
