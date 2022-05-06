package com.github.rush.shopping;

import com.github.rush.services.ordering.GetOrderedShoppingListRequest;
import com.github.rush.services.ordering.GetOrderedShoppingListResponse;
import com.github.rush.services.ordering.OrderingServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ShoppingServerApplication implements CommandLineRunner {

  @GrpcClient("ordering") @LoadBalanced
  private OrderingServiceGrpc.OrderingServiceBlockingStub orderingClient;

  public static void main(String[] args) {
    SpringApplication.run(ShoppingServerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    log.debug("Leci request do {}", orderingClient);
    GetOrderedShoppingListResponse response = orderingClient.getOrderedShoppingList(GetOrderedShoppingListRequest.getDefaultInstance());
    log.debug("Response: {}", response);
  }
}
