package com.github.rush.ordering.service.rpc;

import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import org.springframework.stereotype.Service;

@Service
public class GetOrderedShoppingListProviderImpl implements GetOrderedShoppingListProvider {

    @Override
    public GetOrderedShoppingListResponse getOrderedShoppingList(GetOrderedShoppingListRequest request) {
        return GetOrderedShoppingListResponse.getDefaultInstance();
    }
}
