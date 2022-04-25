package com.github.rush.accounts.service.rpc;

import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;

public interface VerifyAccountProvider {

    VerifyAccountResponse verifyAccount(VerifyAccountRequest request);

}
