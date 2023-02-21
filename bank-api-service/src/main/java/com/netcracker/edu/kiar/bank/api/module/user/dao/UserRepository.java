package com.netcracker.edu.kiar.bank.api.module.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

}
