package com.github.rush.ordering.service;

import com.github.rush.ordering.service.rpc.getorderedshoppinglist.GetOrderedShoppingListProvider;
import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import com.github.rush.services.ordering.OrderingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class OrderingServiceImpl extends OrderingServiceGrpc.OrderingServiceImplBase {

    private final GetOrderedShoppingListProvider getOrderedShoppingListProvider;

    public OrderingServiceImpl(GetOrderedShoppingListProvider getOrderedShoppingListProvider) {
        this.getOrderedShoppingListProvider = getOrderedShoppingListProvider;
    }

    @Override
    public void getOrderedShoppingList(GetOrderedShoppingListRequest request, StreamObserver<GetOrderedShoppingListResponse> responseObserver) {
        responseObserver.onNext(getOrderedShoppingListProvider.getOrderedShoppingList(request));
        responseObserver.onCompleted();
    }
}
