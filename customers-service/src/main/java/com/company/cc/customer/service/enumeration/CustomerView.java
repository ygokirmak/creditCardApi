package com.company.cc.customer.service.enumeration;

public enum CustomerView {

    BASIC ("BASIC"),
    FULL ("FULL");

    private final String name;

    private CustomerView(String s) {
        name = s;
    }

}
