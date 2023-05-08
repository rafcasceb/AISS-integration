package aiss.gitminer.controller;

import aiss.gitminer.exceptions.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.UserRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "User management")
@RestController
@RequestMapping("gitminer")
public class UserController {

    @Autowired
    UserRepository repository;
    @Autowired
    CommentRepository commentRepository;


    @Operation(
            summary = "Retrieve a list of all users.",
            description = "Retrieve a list of all users.",
            tags = {"users", "user", "get", "find", "all"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @GetMapping("/users")
    public List<User> findALl(
                              @Parameter(description = "Page to retrieve.") @RequestParam(defaultValue = "0")int page,
                              @Parameter(description = "Order by activity.") @RequestParam(value = "activeusers", required = false) boolean activeUsers,
                              @RequestParam(value = "order", required = false) String order,
                              @Parameter(description = "Size of the content of the page.") @RequestParam(defaultValue = "10")int size){

        Page<User> pageUsers;
        Pageable paging;
        List<User> users;
        if (order != null){
            if(order.startsWith("-"))
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else{
            paging = PageRequest.of(page, size);
        }
        users = repository.findAll(paging).getContent();

        if(activeUsers == true){
            users = users.stream()
                    .sorted(Comparator.comparing(x -> - commentRepository.findByAuthor(x,paging).getContent().size())).toList();
        }

        return users;
    }



    @Operation(
            summary = "Retrieve a user by ID.",
            description = "Retrieve a user based on the ID.",
            tags = {"users", "user", "get", "find", "one"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @GetMapping("/users/{id}")
    public User findOne(@Parameter(description = "ID of user to be retrieved.") @PathVariable String id)
        throws UserNotFoundException {

        Optional<User> user = repository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        return user.get();
    }



    @Operation(
            summary = "Retrieve a user by name.",
            description = "Retrieve a user based on the name.",
            tags = {"users", "user", "get", "find", "one", "name"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @GetMapping("user/{name}")
    public User findOneByName(@Parameter(description = "Name of user to be retrieved.") @PathVariable String name)
            throws UserNotFoundException {

        Optional<User> user = repository.findByusername(name);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        return user.get();
    }



    @Operation(
            summary = "Create a user.",
            description = "Create a user.",
            tags = {"users", "user", "post", "create"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User create(@Parameter(description = "User to be created.") @Valid @RequestBody User user){
        User _user = repository
                .save(new User(user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getAvatarUrl(),
                        user.getWebUrl()));
        return _user;
    }




    @Operation(
            summary = "Updates a user.",
            description = "Updates a user.",
            tags = {"users", "user", "put", "update"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}")
    public void update(@Parameter(description = "Updated data for user.") @Valid @RequestBody User updated,
                       @Parameter(description = "Id of user to be updated.") @PathVariable String id) {
        Optional<User> userData = repository.findById(id);
        User _user = userData.get();
        _user.setId(updated.getId());
        _user.setUsername(updated.getUsername());
        _user.setName(updated.getName());
        _user.setAvatarUrl(updated.getAvatarUrl());
        _user.setWebUrl(updated.getWebUrl());

        repository.save(_user);
    }



    @Operation(
            summary = "Deletes a user.",
            description = "Deletes a user.",
            tags = {"users", "user", "destroy", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema(implementation = User.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = User.class))})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void delete(@Parameter(description = "Id of user to deleted.") @PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }

}
