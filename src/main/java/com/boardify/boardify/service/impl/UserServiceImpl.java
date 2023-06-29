package com.boardify.boardify.service.impl;

import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);

    }

    @Override
    public Optional<User> findUserByID(Long id) {
        return userRepository.findById(id);
    }
}
