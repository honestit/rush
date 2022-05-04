package com.github.rush.products.service.rpc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductRepository Specification")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository classUnderTest;

    @DisplayName("- should return product")
    @Test
    public void shouldReturnProduct() {
        ProductEntity product1 = ProductEntity.builder().id(1L).name("name 1").text("text 1").build();
        ProductEntity product2 = ProductEntity.builder().id(2L).name("name 2").text("text 2").build();
        entityManager.merge(product1);
        entityManager.merge(product2);

        Optional<ProductEntity> product = classUnderTest.findById(1L);

        assertThat(product.get()).isEqualTo(product1);
    }
}