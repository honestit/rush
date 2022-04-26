package com.github.rush.products.service.rpc;

import com.github.rush.model.product.Product;
import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("GetOrderedShoppingListProviderImpl Specification")
@ExtendWith(MockitoExtension.class)
class FindProductProviderImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductProviderImpl classUnderTest;

    private final static long PRODUCT_ID = 123L;

    @DisplayName("- should return product")
    @Test
    public void shouldReturnProduct() {
        ProductRequest request = ProductRequest.newBuilder().setId(PRODUCT_ID).build();

        ProductResponse expectedResponse = ProductResponse.newBuilder().setProduct(Product.newBuilder().setText("text1").setName("name1").build()).build();

        when(productRepository.findProduct(PRODUCT_ID)).thenReturn(ProductEntity.builder().text("text1").name("name1").build());

        ProductResponse response = classUnderTest.findProduct(request);

        assertThat(response).isEqualTo(expectedResponse);

    }


    @DisplayName("- should not return product because wasn't found")
    @Test
    public void shouldNotReturnProductBecauseNotFound() {
        ProductRequest request = ProductRequest.newBuilder().setId(PRODUCT_ID).build();

        when(productRepository.findProduct(PRODUCT_ID)).thenReturn(null);

        ProductResponse response = classUnderTest.findProduct(request);

        assertThat(response).isEqualToDefaultInstance();
    }


}