package com.github.rush.accounts.service.rpc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;


@DisplayName("AccountRepository Specification")
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AccountRepository classUnderTest;

    private final static String USERNAME = "RushUser";

    @DisplayName("- should return account")
    @Test
    public void shouldReturnAccount() {
        AccountEntity account = AccountEntity.builder().id(1L).username(USERNAME)
                .email("rush@rush.com").build();
        AccountEntity account2 = AccountEntity.builder().id(2L).username(USERNAME + "2")
                .email("rush2@rush.com").build();

        entityManager.merge(account);
        entityManager.merge(account2);

        Optional<AccountEntity> product = classUnderTest.findByUsername(USERNAME);

        assertThat(product.get()).isEqualTo(account);
    }

}