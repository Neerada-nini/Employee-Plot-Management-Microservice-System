package com.microservices.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = false, length = 18)
    private String accNo;

    @Column(nullable = false, length = 6)
    private String bankNo;

    @Column(nullable = false, length = 11)
    private String ifsc;

    @Column(nullable = false, length = 100)
    private String address;

    private String dateTime;

    @Column(nullable = false)
    private String employeeId;
}