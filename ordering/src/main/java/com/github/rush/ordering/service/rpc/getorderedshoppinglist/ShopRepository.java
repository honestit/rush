package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    List<ShopEntity> findAllByProductOrderings_NameContains(String productName);
}
