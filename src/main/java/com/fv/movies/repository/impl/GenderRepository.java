package com.fv.movies.repository.impl;


import com.fv.movies.entity.Gender;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class GenderRepository implements PanacheRepository<Gender> {

}