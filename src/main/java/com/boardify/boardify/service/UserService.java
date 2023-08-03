package com.boardify.boardify.service;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Transaction;
import com.boardify.boardify.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    void changeAccountStatus(String email, String accStatus);

    UserDto convertEntityToDto(User user);

    void editLoggedInUser(String email, UserDto userDto);

    List<UserDto> findAllNonAdmins();
    List<UserDto> findAllPlayers();
    List<UserDto> findAllOrganizers();

    void saveUserObj(User user);
    void deleteUserById(Long id);
    User findByUserId(Long userid);
}
