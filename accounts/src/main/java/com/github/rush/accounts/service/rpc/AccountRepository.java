package com.github.rush.accounts.service.rpc;

import com.github.rush.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByUsername(String username);
}
