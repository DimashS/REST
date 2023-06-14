package com.dimash.spring.restApp.repositories;

import com.dimash.spring.restApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// для создания Spring Boot метода который будет шаблоном для вызова метода, достающего человека из бд
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
