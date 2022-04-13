package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import com.github.rush.model.ordering.Group;
import com.github.rush.model.ordering.Record;
import com.github.rush.model.ordering.Shop;
import com.github.rush.model.shopping.ShoppingItem;
import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetOrderedShoppingListProviderImpl implements GetOrderedShoppingListProvider {

  private final ShopRepository shopRepository;

  @Override
  @Transactional
  public GetOrderedShoppingListResponse getOrderedShoppingList(
      GetOrderedShoppingListRequest request) {
    GetOrderedShoppingListResponse.Builder responseBuilder =
        GetOrderedShoppingListResponse.newBuilder();
    // Fetch a list of shops for each shopping item.
    Map<ShoppingItem, List<ShopEntity>> itemsForShopMap =
        request.getShoppingList().getShoppingItemsList().stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    shoppingItem ->
                        shopRepository.findAllByProductOrderings_NameContains(
                            shoppingItem.getName())));
    log.debug("Mapping items to shop availability: {}", itemsForShopMap);

    // Build a reverse map with all items available in specific shop, without duplicated items
    // between shops
    Map<ShopEntity, List<ShoppingItem>> shopForItemsMap = new HashMap<>();
    itemsForShopMap.forEach(
        (key, value) ->
            value.forEach(
                shop ->
                    shopForItemsMap.merge(
                        shop,
                        new ArrayList<>(List.of(key)),
                        (oldItems, newItems) -> {
                          oldItems.addAll(newItems);
                          return oldItems;
                        })));
    log.debug("Mapping shops to available items: {}", shopForItemsMap);

    // Remove duplicated items from other shop lists, while shops are grouped by having most
    // products available.
    Set<ShoppingItem> existingItems = new HashSet<>();
    shopForItemsMap.entrySet().stream()
        .sorted(
            Comparator.comparingInt(
                    (Map.Entry<ShopEntity, List<ShoppingItem>> entry) -> entry.getValue().size())
                .reversed())
        .forEach(entry -> entry.getValue().removeIf(Predicate.not(existingItems::add)));
    log.debug("Mapping shops to available items with removed duplicates: {}", shopForItemsMap);

    // Build map of products ordering
    Map<ShopEntity, Map<String, Integer>> orderingMap =
        shopForItemsMap.entrySet().stream()
            .peek(entry -> log.debug("Entry for products ordering: {}", entry))
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        entry.getKey().getProductOrderings().stream()
                            .peek(
                                productOrdering ->
                                    log.debug(
                                        "Product ordering {} for shop {}",
                                        productOrdering,
                                        entry.getKey()))
                            .collect(
                                Collectors.toMap(
                                    ProductOrdering::getName,
                                    ProductOrdering::getOrderValueMultipliedToInt))));
    log.debug("Ordering of products in shop: {}", orderingMap);

    // Build groups for each shop
    Map<ShopEntity, Integer> positionMap =
        shopForItemsMap.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> 0));
    log.debug("Default positions for shop: {}", positionMap);
    shopForItemsMap.entrySet().stream()
        .filter(entry -> !entry.getValue().isEmpty())
        .peek(entry -> log.debug("Building group for shop {}", entry.getKey()))
        .map(
            entry ->
                Group.newBuilder()
                    .setName("Group ".concat(entry.getKey().getName()))
                    .setShop(
                        Shop.newBuilder()
                            .setId(String.valueOf(entry.getKey().getId()))
                            .setName(entry.getKey().getName())
                            .build())
                    .addAllRecord(
                        entry.getValue().stream()
                            .peek(
                                shoppingItem ->
                                    log.debug("- shopping item available: {}", shoppingItem))
                            .sorted(
                                Comparator.comparingInt(
                                    shoppingItem ->
                                        orderingMap
                                            .get(entry.getKey())
                                            .get(shoppingItem.getName())))
                            .map(
                                shoppingItem ->
                                    Record.newBuilder()
                                        .setShoppingItem(shoppingItem)
                                        .setPosition(
                                            positionMap.merge(entry.getKey(), 1, Integer::sum))
                                        .build())
                            .collect(Collectors.toList()))
                    .build())
        .forEach(responseBuilder::addGroup);
    return responseBuilder.build();
  }
}
