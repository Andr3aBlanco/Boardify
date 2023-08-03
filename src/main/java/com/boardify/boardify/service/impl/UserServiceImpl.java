package com.boardify.boardify.service.impl;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.Role;
import com.boardify.boardify.entities.User;
import com.boardify.boardify.repository.RoleRepository;
import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setCountry(userDto.getCountry());
        user.setZipCode(userDto.getZipCode());
        user.setPhone(userDto.getPhone());
        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_BASIC");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        user.setAccountStatus("Okay");
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAllNonAdmins() {
        List<User> nonAdminUsers = userRepository.findAllNonAdmins();
        return nonAdminUsers.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    public UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setAddress(user.getAddress());
        userDto.setCity(user.getCity());
        userDto.setState(user.getState());
        userDto.setCountry(user.getCountry());
        userDto.setZipCode(user.getZipCode());
        userDto.setPhone(user.getPhone());
        userDto.setAccountStatus(user.getAccountStatus());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_BASIC");
        return roleRepository.save(role);
    }

    // For changing accountStatus
    public void changeAccountStatus(String email, String accStatus) {
         User user = userRepository.findByEmail(email);

         user.setAccountStatus(accStatus);
         userRepository.save(user);
    }

    public void editLoggedInUser(String email, UserDto userDto) {
        User user = userRepository.findByEmail(email);
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setState(userDto.getState());

        System.out.println(userDto.getState());
        user.setCountry(userDto.getCountry());
        user.setZipCode(userDto.getZipCode());
        user.setPhone(userDto.getPhone());
        System.out.println("Step 1.5");
        userRepository.save(user);
    }

    public void saveUserObj(User user) {
        userRepository.save(user);
    }


    @Override
    public List<UserDto> findAllPlayers() {
        Role playerRole = roleRepository.findByName("ROLE_PLAYER");
        return playerRole.getUsers().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findAllOrganizers() {
        Role organizerRole = roleRepository.findByName("ROLE_ORGANIZER");
        return organizerRole.getUsers().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findByUserId(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isEmpty()) {
            return userOpt.get();
        } else {
            return null;
        }
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
