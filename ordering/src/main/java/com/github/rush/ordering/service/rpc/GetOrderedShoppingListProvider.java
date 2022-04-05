package com.github.rush.ordering.service.rpc;

import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;

public interface GetOrderedShoppingListProvider {

    GetOrderedShoppingListResponse getOrderedShoppingList(GetOrderedShoppingListRequest request);
}
