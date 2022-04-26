package com.github.rush.products.service.rpc;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String text;

    @NotNull
    private String name;

    @ToString.Exclude
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "products_aliases", joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private List<String> aliases = new ArrayList<>();

    @ToString.Exclude
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "categories_aliases", joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private List<String> categories = new ArrayList<>();

//    private Map<Integer, String> attribute = new ArrayList<>();


}
