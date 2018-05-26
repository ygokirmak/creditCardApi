package com.company.cc.account.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity( name = "account")
public class Account implements Serializable {

    private static final long serialVersionUID = 7799738024850692428L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Account setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Account setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return  Objects.equals(id, account.id) &&
                Objects.equals(customerId, account.customerId) &&
                Objects.equals(name, account.name) &&
                Objects.equals(surname, account.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, name, surname);
    }
}
