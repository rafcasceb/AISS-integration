package aiss.gitminer.controller;

import aiss.gitminer.exceptions.ProjectNotFoundException;
import aiss.gitminer.exceptions.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("gitminer")
public class UserController {
    @Autowired
    UserRepository repository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/users")
    public List<User> findALl(@RequestParam(value = "activefirst",required = false) Boolean activeFirst,
                              @RequestParam(defaultValue = "0")int page,
                              @RequestParam(defaultValue = "10")int size){

        Page<User> pageUsers;
        Pageable paging = PageRequest.of(page,size);
        pageUsers = repository.findAll(paging);
        List<User> users = pageUsers.getContent();
        if (activeFirst != null) {
            if (!activeFirst) {
                users.sort(Comparator.comparing(x ->
                        commentRepository.findAll().stream()
                                .filter(y -> y.getAuthor().getUsername().equals(x.getUsername()))
                                .toList().size()));
            } else {

            }
        }
        return users;
    }

    @GetMapping("/users/{id}")
    public User findOne(@PathVariable String id)
        throws UserNotFoundException {

        Optional<User> user = repository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        return user.get();
    }

    @GetMapping("user/{name}")
    public User findOnebyName(@PathVariable String name)
            throws UserNotFoundException {

        Optional<User> user = repository.findByusername(name);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public User create(@Valid @RequestBody User user){
        User _user = repository
                .save(new User(user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getAvatarUrl(),
                        user.getWebUrl()));
        return _user;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{id}")
    public void update(@Valid @RequestBody User updated, @PathVariable String id) {
        Optional<User> userData = repository.findById(id);
        User _user = userData.get();
        _user.setId(updated.getId());
        _user.setUsername(updated.getUsername());
        _user.setName(updated.getName());
        _user.setAvatarUrl(updated.getAvatarUrl());
        _user.setWebUrl(updated.getWebUrl());

        repository.save(_user);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }


}
