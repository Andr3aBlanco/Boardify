package com.boardify.boardify.service;

import com.boardify.boardify.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAllUsers();

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    Optional<User> findUserByID(Long id);


}
