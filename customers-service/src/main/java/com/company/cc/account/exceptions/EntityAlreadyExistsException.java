package com.company.cc.account.exceptions;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(Class clazz, long id) {
        super(String.format("%s entity is already exists with {id:%d}", StringUtils.capitalize(clazz.getSimpleName()) , id ));
    }
}