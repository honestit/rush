package com.github.rush.products.service.rpc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@DisplayName("ProductRepository Specification")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository classUnderTest;

    private static long PRODUCT_ID = 1L;

    @DisplayName("- should return product")
    @Test
    public void shouldReturnProduct() {

    }

}