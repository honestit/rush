package com.github.rush.accounts.service.rpc;

import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;

public class VerifyAccountProviderImpl implements VerifyAccountProvider{

    @Override
    public VerifyAccountResponse verifyAccount(VerifyAccountRequest request) {
        return VerifyAccountResponse.getDefaultInstance();
    }
}
