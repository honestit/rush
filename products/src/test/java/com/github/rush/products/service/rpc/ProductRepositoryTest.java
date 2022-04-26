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
//        ProductEntity product1 = ProductEntity.builder()
//                .id(PRODUCT_ID)
//                .name("name 1")
//                .text("text 1")
//                .aliases(
//                        List.of(
//                                "alias1", "alias2"
//                        )
//                )
//                .categories(
//                        List.of(
//                                "category11", "category12"
//                        )
//                )
//                .build();
//
//        ProductEntity product2 = ProductEntity.builder()
//                .id(2L)
//                .name("name 2")
//                .text("text 2")
//                .aliases(
//                        List.of(
//                                "alias2", "alias2"
//                        )
//                )
//                .categories(
//                        List.of(
//                                "category21", "category22"
//                        )
//                )
//                .build();
//
//        entityManager.persist(product1);
//        entityManager.persist(product2);
//
//        ProductEntity product = classUnderTest.findProduct(PRODUCT_ID);
//
//        assertThat(product).isEqualTo(product1);

    }

}