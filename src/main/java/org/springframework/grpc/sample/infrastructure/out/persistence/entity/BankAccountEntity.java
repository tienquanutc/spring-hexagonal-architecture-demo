package org.springframework.grpc.sample.infrastructure.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.grpc.sample.domain.vo.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "BANK_ACCOUNT")
@Accessors(chain = true)
public class BankAccountEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "ID")
    private UUID id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "CURRENCY")
    private Currency currency = Currency.USD;

    @Column(name = "BALANCE")
    @DecimalMin(value = "0.00", message = "Balance must be >= 0")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}
