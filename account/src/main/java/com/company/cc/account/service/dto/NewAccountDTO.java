package com.company.cc.account.service.dto;

public class NewAccountDTO {

    private Long customerId;

    private long initialCredit;

    public Long getCustomerId() {
        return customerId;
    }

    public NewAccountDTO setCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public long getInitialCredit() {
        return initialCredit;
    }

    public NewAccountDTO setInitialCredit(long initialCredit) {
        this.initialCredit = initialCredit;
        return this;
    }
}
