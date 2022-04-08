package com.github.rush.products.service;

import com.github.rush.products.service.rpc.FindProductProvider;
import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import com.github.rush.services.products.ProductsServiceGrpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductsServiceImpl extends ProductsServiceGrpc.ProductsServiceImplBase {

    private final FindProductProvider findProductProvider;

    public ProductsServiceImpl(FindProductProvider findProductProvider) {
        this.findProductProvider = findProductProvider;
    }

    @Override
    public void findProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        responseObserver.onNext(findProductProvider.findProduct(request));
        responseObserver.onCompleted();
    }
}
