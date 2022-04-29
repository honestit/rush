package com.github.rush.accounts.service.rpc;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "accounts")
@Getter @Setter
@ToString
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String email;
}
