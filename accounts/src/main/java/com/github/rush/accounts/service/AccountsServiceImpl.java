package com.github.rush.accounts.service;

import com.github.rush.accounts.service.rpc.VerifyAccountProvider;
import com.github.rush.services.account.AccountServiceGrpc;
import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AccountsServiceImpl extends AccountServiceGrpc.AccountServiceImplBase {

    private final VerifyAccountProvider verifyAccountProvider;

    public AccountsServiceImpl(VerifyAccountProvider verifyAccountProvider) {
        this.verifyAccountProvider = verifyAccountProvider;
    }

    @Override
    public void verifyAccount(VerifyAccountRequest request, StreamObserver<VerifyAccountResponse> responseObserver) {
        responseObserver.onNext(verifyAccountProvider.verifyAccount(request));
        responseObserver.onCompleted();
    }
}
