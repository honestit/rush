package com.github.rush.accounts.service;

import com.github.rush.accounts.service.rpc.VerifyAccountProvider;
import com.github.rush.model.account.Account;
import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@DisplayName("AccountsService Specification")
@ExtendWith(SpringExtension.class)
@Import(AccountsServiceImpl.class)
public class AccountsServiceImplTest {

    @MockBean
    private VerifyAccountProvider verifyAccountProvider;

    @Autowired
    private AccountsServiceImpl accountsService;

    @DisplayName("- should return false for account verification")
    @Test
    public void sanityCheck() throws Exception {
        VerifyAccountRequest request = VerifyAccountRequest.newBuilder()
                .setAccount(Account.newBuilder()
                        .setId(123L).build())
                .build();

        when(verifyAccountProvider.verifyAccount(request))
                .thenReturn(VerifyAccountResponse.getDefaultInstance());

        StreamRecorder<VerifyAccountResponse> responseObserver = StreamRecorder.create();
        accountsService.verifyAccount(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertThat(responseObserver.getError()).isNull();
        assertThat(responseObserver.getValues()).hasSize(1);
        assertThat(responseObserver.getValues().get(0).getVerifyResult());
    }

    @DisplayName("- should return true for account verification")
    @Test
    public void shouldReturnProduct() throws Exception {
        VerifyAccountRequest request = VerifyAccountRequest.newBuilder()
                .setAccount(Account.newBuilder()
                        .setId(123L).build())
                .build();
        when(verifyAccountProvider.verifyAccount(request)).thenReturn(VerifyAccountResponse
                .newBuilder().setVerifyResult(true).build());

        StreamRecorder<VerifyAccountResponse> responseObserver = StreamRecorder.create();
        accountsService.verifyAccount(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        assertThat(responseObserver.getValues()).hasSize(1);
        assertThat(responseObserver.getValues().get(0)).isEqualTo(VerifyAccountResponse.newBuilder()
                .setVerifyResult(true)
                .build());
    }
}