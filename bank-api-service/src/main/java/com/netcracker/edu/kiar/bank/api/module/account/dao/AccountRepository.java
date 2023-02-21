package com.netcracker.edu.kiar.bank.api.module.account.dao;

import com.netcracker.edu.kiar.bank.api.module.user.dao.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    Optional<AccountEntity> findByUser(UserEntity user);

}
