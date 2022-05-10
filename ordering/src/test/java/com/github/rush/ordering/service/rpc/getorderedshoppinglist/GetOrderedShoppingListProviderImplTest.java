package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import com.github.rush.model.ordering.Group;
import com.github.rush.model.ordering.Record;
import com.github.rush.model.ordering.Shop;
import com.github.rush.model.shopping.ShoppingItem;
import com.github.rush.model.shopping.ShoppingList;
import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("GetOrderedShoppingListProviderImpl Specification")
@ExtendWith(MockitoExtension.class)
class GetOrderedShoppingListProviderImplTest {

  private static final String PRODUCT_NAME_UNAVAILABLE_IN_SHOP = "Not available product";
  private static final ShoppingItem SHOPPING_ITEM_WITH_UNAVAILABLE_PRODUCT =
      ShoppingItem.newBuilder().setName(PRODUCT_NAME_UNAVAILABLE_IN_SHOP).build();
  private static final ShoppingList SHOPPING_LIST_WITH_UNAVAILABLE_ITEM =
      ShoppingList.newBuilder()
          .setName("Shopping list")
          .addShoppingItems(SHOPPING_ITEM_WITH_UNAVAILABLE_PRODUCT)
          .build();
  private static final String PRODUCT_1_NAME_AVAILABLE_IN_SHOP = "Available product 1";
  private static final String PRODUCT_2_NAME_AVAILABLE_IN_SHOP = "Available product 2";
  private static final String PRODUCT_3_NAME_AVAILABLE_IN_SHOP = "Available product 3";
  private static final ShoppingItem SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1 =
      ShoppingItem.newBuilder().setName(PRODUCT_1_NAME_AVAILABLE_IN_SHOP).build();
  private static final ShoppingItem SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2 =
      ShoppingItem.newBuilder().setName(PRODUCT_2_NAME_AVAILABLE_IN_SHOP).build();
  private static final ShoppingItem SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_3 =
      ShoppingItem.newBuilder().setName(PRODUCT_3_NAME_AVAILABLE_IN_SHOP).build();
  private static final ShoppingList SHOPPING_LIST_WITH_1_AVAILABLE_ITEM =
      ShoppingList.newBuilder()
          .setName("Shopping list")
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1)
          .build();
  private static final ShoppingList SHOPPING_LIST_WITH_2_AVAILABLE_ITEMS =
      ShoppingList.newBuilder()
          .setName("Shopping list")
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1)
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2)
          .build();
  private static final ShoppingList SHOPPING_LIST_WITH_3_AVAILABLE_ITEMS =
      ShoppingList.newBuilder()
          .setName("Shopping list with 3 items")
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1)
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2)
          .addShoppingItems(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_3)
          .build();
  private static final Shop SHOP_1 = Shop.newBuilder().setId("1").setName("Shop 1").build();
  private static final Shop SHOP_2 = Shop.newBuilder().setId("2").setName("Shop 2").build();

  @Mock private ShopRepository shopRepository;

  @InjectMocks private GetOrderedShoppingListProviderImpl classUnderTest;

  @DisplayName("- should not return any groups when no shops were found")
  @Test
  public void shouldNotReturnAnyGroups_whenNoShopsFound() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_UNAVAILABLE_ITEM)
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_NAME_UNAVAILABLE_IN_SHOP))
        .thenReturn(Collections.emptyList());

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);

    assertThat(response).isEqualToDefaultInstance();
  }

  @DisplayName("- should return one group when only one item was found")
  @Test
  public void shouldReturnOneGroup_whenOnlyOneItemWasFound() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_1_AVAILABLE_ITEM)
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_1_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(List.of(ShopEntity.builder().id(1L).name("Shop 1").build()));

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);

    assertThatResponseHasGroupForShopWithItems(
        response, SHOP_1, SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1);
  }

  @DisplayName("- should return one group for each item found in distinct shops")
  @Test
  public void shouldReturnOneGroupForEachItemFoundInDistinctShops() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_2_AVAILABLE_ITEMS)
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_1_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(List.of(ShopEntity.builder().id(1L).name("Shop 1").build()));
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_2_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(List.of(ShopEntity.builder().id(2L).name("Shop 2").build()));

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);
    assertThat(response.getGroupList()).hasSize(2);
    assertThatResponseHasGroupForShopWithItems(
        response, SHOP_1, SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1);
    assertThatResponseHasGroupForShopWithItems(
        response, SHOP_2, SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2);
  }

  @DisplayName("- should return only one group when product was found in different shops")
  @Test
  public void shouldReturnOnlyOneGroupWhenProductWasFoundInDifferentShops() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_1_AVAILABLE_ITEM)
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_1_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(
            List.of(
                ShopEntity.builder().id(1L).name("Shop 1").build(),
                ShopEntity.builder().id(2L).name("Shop 2").build()));

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);

    assertThat(response.getGroupList()).hasSize(1);
    assertThatResponseHasGroupForShopWithItems(
        response, SHOP_1, SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1);
  }

  @Disabled
  @DisplayName("- should return group with records for each found product")
  @Test
  public void shouldReturnGroupWithRecordsForEachFoundProduct() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_2_AVAILABLE_ITEMS)
            .build();
    ShopEntity shopEntity =
        ShopEntity.builder()
            .id(1L)
            .name("Shop 1")
            .productOrderings(
                List.of(
                    ProductOrdering.builder()
                        .name(PRODUCT_1_NAME_AVAILABLE_IN_SHOP)
                        .orderValue(1.0)
                        .build(),
                    ProductOrdering.builder()
                        .name(PRODUCT_2_NAME_AVAILABLE_IN_SHOP)
                        .orderValue(1.0)
                        .build()))
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_1_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(List.of(shopEntity));
    when(shopRepository.findAllByProductOrderings_NameContains(PRODUCT_2_NAME_AVAILABLE_IN_SHOP))
        .thenReturn(List.of(shopEntity));

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);

    assertThat(response.getGroupList()).hasSize(1);
    assertThatResponseHasGroupForShopWithItems(
        response,
        SHOP_1,
        SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1,
        SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2);
  }

  @DisplayName("- should sort records within a group based on products position")
  @Test
  public void shouldSortRecordsWithinGroupBasedOnProductsPosition() {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(SHOPPING_LIST_WITH_3_AVAILABLE_ITEMS)
            .build();
    ShopEntity shopEntity =
        ShopEntity.builder()
            .id(1L)
            .name("Shop 1")
            .productOrderings(
                List.of(
                    ProductOrdering.builder()
                        .name(PRODUCT_1_NAME_AVAILABLE_IN_SHOP)
                        .orderValue(2.0)
                        .build(),
                    ProductOrdering.builder()
                        .name(PRODUCT_2_NAME_AVAILABLE_IN_SHOP)
                        .orderValue(1.0)
                        .build(),
                    ProductOrdering.builder()
                        .name(PRODUCT_3_NAME_AVAILABLE_IN_SHOP)
                        .orderValue(3.0)
                        .build()))
            .build();
    when(shopRepository.findAllByProductOrderings_NameContains(ArgumentMatchers.anyString()))
        .thenReturn(List.of(shopEntity));

    GetOrderedShoppingListResponse response = classUnderTest.getOrderedShoppingList(request);

    assertThat(response.getGroupList()).hasSize(1);
    assertThat(response.getGroup(0).getRecordList()).hasSize(3);
    Assertions.assertThat(response.getGroup(0).getRecordList())
        .containsExactly(
            Record.newBuilder()
                .setShoppingItem(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_2)
                .setPosition(1)
                .build(),
            Record.newBuilder()
                .setShoppingItem(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_1)
                .setPosition(2)
                .build(),
            Record.newBuilder()
                .setShoppingItem(SHOPPING_ITEM_WITH_AVAILABLE_PRODUCT_3)
                .setPosition(3)
                .build());
  }

  private void assertThatResponseHasGroupForShopWithItems(
      GetOrderedShoppingListResponse response, Shop shop, ShoppingItem... shoppingItems) {
    Group.Builder builder =
        Group.newBuilder().setName("Group ".concat(shop.getName())).setShop(shop);
    IntStream.iterate(0, i -> i < shoppingItems.length, i -> i + 1)
        .mapToObj(
            i -> Record.newBuilder().setShoppingItem(shoppingItems[i]).setPosition(i + 1).build())
        .forEach(builder::addRecord);
    assertThat(response.getGroupList()).contains(builder.build());
  }
}
