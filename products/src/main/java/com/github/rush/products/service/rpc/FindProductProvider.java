package com.github.rush.products.service.rpc;

import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;

public interface FindProductProvider {
    ProductResponse findProduct(ProductRequest productRequest);
}
