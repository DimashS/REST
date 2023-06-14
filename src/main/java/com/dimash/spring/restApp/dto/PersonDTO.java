package com.dimash.spring.restApp.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonDTO {
    // те поля которые нас интересуют при общении клиента с сервером
    @NotEmpty(message = "Name shouldn't be empty")
    @Size(min = 2, max = 30, message = "name could be only between 2 or 30 characters")
    private String name;
    @Min(value = 0, message = "Age should be greater than 0")
    private int age;

    @Email
    @NotEmpty(message = "email shouldn't be empty")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
