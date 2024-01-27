package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    private String name;
    @Column(length = 64, nullable = false, unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;
    private boolean enabled;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();
}
