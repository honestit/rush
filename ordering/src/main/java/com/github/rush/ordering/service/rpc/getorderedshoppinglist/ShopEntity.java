package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shops")
@Getter @Setter @ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;

    @ToString.Exclude
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "shops_products_ordering", joinColumns = @JoinColumn(name = "shop_id"))
    @Builder.Default
    private List<ProductOrdering> productOrderings = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShopEntity that = (ShopEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
