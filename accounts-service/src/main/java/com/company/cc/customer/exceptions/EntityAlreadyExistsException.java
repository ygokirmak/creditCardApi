package com.company.cc.customer.exceptions;

import org.springframework.util.StringUtils;

public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(Class clazz, long id) {
        super(String.format("%s entity is already exists with {id:%d}", StringUtils.capitalize(clazz.getSimpleName()) , id ));
    }
}