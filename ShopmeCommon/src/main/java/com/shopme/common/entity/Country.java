package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "countries")
@Getter
@Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(nullable = false, length = 5, unique = true)
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> state;

    public Country() {
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }
}