package com.example.chirp.app.springdata;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * There is no implementation of this UserRepository interface in the project.
 * Spring will auto generate a class for this.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.username = ?1")
    List<UserEntity> findByUsername(String username);

}