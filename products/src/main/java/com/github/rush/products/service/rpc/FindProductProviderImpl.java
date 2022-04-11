package com.github.rush.products.service.rpc;

import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class FindProductProviderImpl implements FindProductProvider{
    @Override
    public ProductResponse findProduct(ProductRequest productRequest) {
        return ProductResponse.getDefaultInstance();
    }
}
