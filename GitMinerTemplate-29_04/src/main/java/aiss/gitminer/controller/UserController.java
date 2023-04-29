package aiss.gitminer.controller;

import aiss.gitminer.exceptions.ProjectNotFoundException;
import aiss.gitminer.exceptions.UserNotFoundException;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("gitminer/users")
public class UserController {
    @Autowired
    UserRepository repository;

    @GetMapping
    public List<User> findALl(){ return repository.findAll();}

    @GetMapping("/{id}")
    public User findOne(@PathVariable String id)
        throws UserNotFoundException {

        Optional<User> user = repository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        return user.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
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
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }
    }


}
