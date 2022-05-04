package com.github.rush.products.service.rpc;

import com.github.rush.model.product.Product;
import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FindProductProviderImpl implements FindProductProvider {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse findProduct(ProductRequest productRequest) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productRequest.getId());
        if (productEntityOpt.isEmpty()) {
            return ProductResponse.getDefaultInstance();
        }
        ProductEntity productEntity = productEntityOpt.get();
        Product product = Product.newBuilder().setText(productEntity.getText())
                .setName(productEntity.getName())
                .build();
        return ProductResponse.newBuilder().setProduct(product).build();
    }
}
