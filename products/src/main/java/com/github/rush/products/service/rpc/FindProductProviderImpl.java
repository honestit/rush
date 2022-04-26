package com.github.rush.products.service.rpc;

import com.github.rush.model.product.Product;
import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindProductProviderImpl implements FindProductProvider {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse findProduct(ProductRequest productRequest) {
        ProductEntity productEntity = productRepository.findProduct(productRequest.getId());
        if (productEntity == null) {
            return ProductResponse.getDefaultInstance();
        }

        Product product = Product.newBuilder().setText(productEntity.getText())
                .setName(productEntity.getName())
                .build();

        return ProductResponse.newBuilder().setProduct(product).build();
    }
}
