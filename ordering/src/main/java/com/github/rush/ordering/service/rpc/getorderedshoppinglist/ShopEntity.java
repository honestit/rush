package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shops")
@Getter @Setter @ToString(exclude = {"productOrderings"}) @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;

    @ElementCollection
    @CollectionTable(name = "shops_products_ordering")
    @Builder.Default
    private List<ProductOrdering> productOrderings = new ArrayList<>();
}
