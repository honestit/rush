package com.github.rush.accounts.service.rpc;


import com.github.rush.model.account.Account;
import com.github.rush.services.account.VerifyAccountRequest;
import com.github.rush.services.account.VerifyAccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("VerifyAccountProviderImpl Specification")
@ExtendWith(MockitoExtension.class)
class VerifyAccountProviderImplTest {

    private final static long PRODUCT_ID = 123L;
    private final static String USERNAME = "RushUser";
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private VerifyAccountProviderImpl classUnderTest;

    @DisplayName("- should return true")
    @Test
    public void shouldReturnTrue() {
        VerifyAccountRequest request = VerifyAccountRequest.newBuilder()
                .setAccount(Account.newBuilder().setUsername(USERNAME).build()).build();
        VerifyAccountResponse expectedResponse = VerifyAccountResponse.newBuilder()
                .setVerifyResult(true).build();
        when(accountRepository.findByUsername(USERNAME)).thenReturn(Optional.of(AccountEntity
                .builder().username(USERNAME).build()));
        VerifyAccountResponse response = classUnderTest.verifyAccount(request);

        assertThat(response).isEqualTo(expectedResponse);
    }


    @DisplayName("- should return false cause account can't be found")
    @Test
    public void shouldReturnFalseBecauseAccountNotFound() {
        VerifyAccountRequest request = VerifyAccountRequest.newBuilder()
                .setAccount(Account.newBuilder().setUsername(USERNAME).build()).build();
        when(accountRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        VerifyAccountResponse response = classUnderTest.verifyAccount(request);

        assertThat(response).isEqualToDefaultInstance();
    }

}