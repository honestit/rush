package com.github.rush.accounts.service.rpc;

import com.github.rush.model.account.Account;
import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyAccountProviderImpl implements VerifyAccountProvider {

    private final AccountRepository accountRepository;

    @Override
    public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) {
        Account account = request.getAccount();
        if (request.getAccount() == null) {
            log.debug("No account in VerifyAccountRequest");
            return VerifyAccountResponse.getDefaultInstance();
        }
        accountRepository.findByUsername(account.getUsername());
        return VerifyAccountResponse.newBuilder()
                .setVerifyResult(accountRepository.findByUsername(account.getUsername()).isPresent())
                .build();
    }
}
