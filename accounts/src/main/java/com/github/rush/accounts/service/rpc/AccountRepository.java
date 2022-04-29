package com.github.rush.accounts.service.rpc;

import com.github.rush.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Boolean verifyAccount(Account account);
}
