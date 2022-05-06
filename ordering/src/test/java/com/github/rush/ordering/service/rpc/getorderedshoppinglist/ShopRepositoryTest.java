package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ShopRepository Specification")
@DataJpaTest
class ShopRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private ShopRepository classUnderTest;

  @DisplayName("- should return all shops with product orderings of given name")
  @Test
  public void shouldReturnShopsWithProductOrderingOfGivenName() {
    ShopEntity shop1 =
        ShopEntity.builder()
            .name("Shop 1")
            .productOrderings(
                List.of(
                    ProductOrdering.builder().name("Product 1").orderValue(1.0).build(),
                    ProductOrdering.builder().name("Product 2").orderValue(2.0).build()))
            .build();
    ShopEntity shop2 =
        ShopEntity.builder()
            .name("Shop 2")
            .productOrderings(
                List.of(
                    ProductOrdering.builder().name("Product 1").orderValue(3.0).build(),
                    ProductOrdering.builder().name("Product 3").orderValue(4.0).build()))
            .build();
    ShopEntity shop3 =
        ShopEntity.builder()
            .name("Shop 3")
            .productOrderings(
                List.of(ProductOrdering.builder().name("Product 4").orderValue(5.0).build()))
            .build();
    entityManager.persist(shop1);
    entityManager.persist(shop2);
    entityManager.persist(shop3);

    List<ShopEntity> shopList = classUnderTest.findAllByProductOrderings_NameContains("Product 1");

    assertThat(shopList).containsExactly(shop1, shop2);
  }
}
