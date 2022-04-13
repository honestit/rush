package com.github.rush.ordering.service;

import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import com.github.rush.services.ordering.OrderingServiceGrpc;
import com.google.common.truth.extensions.proto.ProtoTruth;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("OrderingService Integration Tests")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
      "grpc.server.inProcessName=test",
      "grpc.server.port=-1",
      "grpc.client.inProcess.address=in-process:test"
    })
@ImportAutoConfiguration(
    classes = {
      GrpcServerAutoConfiguration.class,
      GrpcServerFactoryAutoConfiguration.class,
      GrpcClientAutoConfiguration.class
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderingServiceImplTestIT {

  @GrpcClient("inProcess")
  private OrderingServiceGrpc.OrderingServiceBlockingStub stub;

  @DisplayName("For getting ordered shopping list")
  @Nested
  class GettingOrderedShoppingListTest {

    @DisplayName("- should return default response when there is no shops data")
    @Test
    public void shouldPass() {
      GetOrderedShoppingListResponse response =
          stub.getOrderedShoppingList(GetOrderedShoppingListRequest.getDefaultInstance());

      ProtoTruth.assertThat(response).isEqualToDefaultInstance();
    }
  }
}
