package com.boardify.boardify.controller;

import com.boardify.boardify.entities.User;
import com.boardify.boardify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class UserRestController {

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){

        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity addNewUSer(@RequestBody User user){

        User insertedUser = userService.createUser(user);

        if(insertedUser == null ){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(insertedUser.getId(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody User user){

        userService.updateUser(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable("userID") Long id){
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
