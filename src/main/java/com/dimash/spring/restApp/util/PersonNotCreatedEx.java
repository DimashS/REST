package com.dimash.spring.restApp.util;

public class PersonNotCreatedEx extends RuntimeException {
    public PersonNotCreatedEx(String message) {
        super(message);
    }
}
