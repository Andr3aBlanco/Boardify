package com.boardify.boardify.repository;

import com.boardify.boardify.DTO.UserDto;
import com.boardify.boardify.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(
            value = "SELECT * FROM USERS WHERE id IN " +
                    "(SELECT u.id FROM USERS as u " +
                    "JOIN USERS_ROLES as ur ON u.id = ur.user_id " +
                    "JOIN ROLES as r ON ur.role_id = r.id " +
                    "WHERE r.name != 'ROLE_ADMIN');",
            nativeQuery = true)
    List<User> findAllNonAdmins();

}
