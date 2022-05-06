package com.github.rush.ordering.service;

import com.github.rush.model.ordering.Group;
import com.github.rush.model.ordering.Record;
import com.github.rush.model.shopping.ShoppingItem;
import com.github.rush.model.shopping.ShoppingList;
import com.github.rush.ordering.service.rpc.getorderedshoppinglist.GetOrderedShoppingListProvider;
import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
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
@Import(OrderingServiceImpl.class)
class OrderingServiceImplTest {

  @MockBean private GetOrderedShoppingListProvider getOrderedShoppingListProvider;

  @Autowired private OrderingServiceImpl orderingService;

  @DisplayName("- should return no groups when shopping items were not found")
  @Test
  public void sanityCheck() throws Exception {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(
                ShoppingList.newBuilder()
                    .setName("Shopping list 1")
                    .addShoppingItems(ShoppingItem.newBuilder().setName("Shopping item 1").build())
                    .build())
            .build();
    when(getOrderedShoppingListProvider.getOrderedShoppingList(request))
        .thenReturn(GetOrderedShoppingListResponse.getDefaultInstance());
    StreamRecorder<GetOrderedShoppingListResponse> responseObserver = StreamRecorder.create();

    orderingService.getOrderedShoppingList(request, responseObserver);

    if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
      fail("The call did not terminate in time");
    }
    assertThat(responseObserver.getError()).isNull();
    assertThat(responseObserver.getValues()).hasSize(1);
    assertThat(responseObserver.getValues().get(0).getGroupList()).isEmpty();
  }

  @DisplayName("- should return groups for found shopping items")
  @Test
  public void shouldReturnGroupsForFoundShoppingItems() throws Exception {
    GetOrderedShoppingListRequest request =
        GetOrderedShoppingListRequest.newBuilder()
            .setShoppingList(
                ShoppingList.newBuilder()
                    .setName("Shopping list 1")
                    .addShoppingItems(ShoppingItem.newBuilder().setName("Shopping item 1"))
                    .addShoppingItems(ShoppingItem.newBuilder().setName("Shopping item 2"))
                    .addShoppingItems(ShoppingItem.newBuilder().setName("Shopping item 3"))
                    .build())
            .build();
    when(getOrderedShoppingListProvider.getOrderedShoppingList(request))
        .thenReturn(
            GetOrderedShoppingListResponse.newBuilder()
                .addGroup(
                    Group.newBuilder()
                        .addRecord(
                            Record.newBuilder()
                                .setShoppingItem(
                                    ShoppingItem.newBuilder().setName("Shopping item 1")))
                        .addRecord(
                            Record.newBuilder()
                                .setShoppingItem(
                                    ShoppingItem.newBuilder().setName("Shopping item 3"))))
                .build());
    StreamRecorder<GetOrderedShoppingListResponse> responseObserver = StreamRecorder.create();

    orderingService.getOrderedShoppingList(request, responseObserver);

    if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
      fail("The call did not terminate in time");
    }
    assertThat(responseObserver.getError()).isNull();
    assertThat(responseObserver.getValues()).hasSize(1);
    assertThat(responseObserver.getValues().get(0).getGroupList()).hasSize(1);
    ProtoTruth.assertThat(responseObserver.getValues().get(0).getGroup(0))
        .isEqualTo(
            Group.newBuilder()
                .addRecord(
                    Record.newBuilder()
                        .setShoppingItem(ShoppingItem.newBuilder().setName("Shopping item 1")))
                .addRecord(
                    Record.newBuilder()
                        .setShoppingItem(ShoppingItem.newBuilder().setName("Shopping item 3")))
                .build());
  }
}
