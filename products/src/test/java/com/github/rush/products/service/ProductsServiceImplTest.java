package com.github.rush.products.service;

import com.github.rush.model.product.Product;
import com.github.rush.products.service.rpc.FindProductProvider;
import com.github.rush.services.products.ProductRequest;
import com.github.rush.services.products.ProductResponse;
import com.google.common.truth.extensions.proto.ProtoTruth;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@DisplayName("OrderingService Specification")
@ExtendWith(SpringExtension.class)
@Import(ProductsServiceImpl.class)
public class ProductsServiceImplTest {

    @MockBean private FindProductProvider findProductProvider;

    @Autowired private ProductsServiceImpl productsService;

    @DisplayName("- should return no product was found")
    @Test
    public void sanityCheck() throws Exception {
        ProductRequest request = ProductRequest.newBuilder()
                .setId(123L).build();
        when(findProductProvider.findProduct(request)).thenReturn(
                ProductResponse.getDefaultInstance());
        StreamRecorder<ProductResponse> responseObserver = StreamRecorder.create();

        productsService.findProduct(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertThat(responseObserver.getError()).isNull();
        assertThat(responseObserver.getValues()).hasSize(1);
        assertThat(responseObserver.getValues().get(0).getProduct().getAliasesList()).isEmpty();
        assertThat(responseObserver.getValues().get(0).getProduct().getCategoriesList()).isEmpty();
    }

    @DisplayName("- should return product")
    @Test
    public void shouldReturnProduct() throws Exception {
        ProductRequest request = ProductRequest.newBuilder().setId(123L).build();

        when(findProductProvider.findProduct(request))
                .thenReturn(
                        ProductResponse.newBuilder()
                                .setProduct(Product.newBuilder()
                                        .setText("text 1")
                                        .setName("name 1")
                                        .addAliases("alias 1")
                                        .addAliases("alias 2")
                                        .putAttributes(1, "attribute 1")
                                        .putAttributes(2, "attribute 2")
                                        .addCategories("meat").addCategories("other").build()
                                ).build()
                );

        StreamRecorder<ProductResponse> responseObserver = StreamRecorder.create();
        productsService.findProduct(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }

        assertThat(responseObserver.getError()).isNull();
        assertThat(responseObserver.getValues()).hasSize(1);
        ProtoTruth.assertThat(responseObserver.getValues().get(0)).isEqualTo(
                ProductResponse.newBuilder()
                        .setProduct(Product.newBuilder()
                                .setText("text 1")
                                .setName("name 1")
                                .addAliases("alias 1")
                                .addAliases("alias 2")
                                .putAttributes(1, "attribute 1")
                                .putAttributes(2, "attribute 2")
                                .addCategories("meat").addCategories("other").build()
                        ).build()
        );
    }
}
