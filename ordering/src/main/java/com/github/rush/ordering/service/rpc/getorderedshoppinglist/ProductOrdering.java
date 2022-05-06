package com.github.rush.ordering.service.rpc.getorderedshoppinglist;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter @Setter @ToString @EqualsAndHashCode(of = "name")
@Builder @NoArgsConstructor @AllArgsConstructor
public class ProductOrdering {

    @NotNull
    private String name;
    @Column(name = "order_value", nullable = false)
    private Double orderValue;

    public Integer getOrderValueMultipliedToInt() {
        return (int) (orderValue * 100);
    }
}
