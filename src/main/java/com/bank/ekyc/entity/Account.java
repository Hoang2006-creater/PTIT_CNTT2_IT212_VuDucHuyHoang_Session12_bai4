package com.bank.ekyc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * Database Entity representing a Customer Bank Account.
 * Map with "accounts" table schema.
 *
 * @author Senior QA Automation Engineer
 * @since 1.0.0
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "account_id", updatable = false, nullable = false)
    private UUID accountId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "citizen_id", nullable = false, unique = true, length = 12)
    private String citizenId;

    @Column(name = "account_number", nullable = false, unique = true, length = 10)
    private String accountNumber;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reject_reason")
    private String rejectReason;
}
