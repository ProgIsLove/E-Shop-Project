package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "categories")
@Setter
@Getter
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Category() {
    }

    public Category(Integer id, String name, String alias, String image, boolean enabled, Category parent, Set<Category> children) {
        this.id = id;
        this.name = name;
        this.alias = name;
        this.image = "default.png";
        this.enabled = enabled;
        this.parent = parent;
        this.children = children;
    }

    public static Category copyIdAndName(Category category) {
        return Category.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = null;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public void addSubCategory(Category subcategory) {
        this.children.add(subcategory);
        subcategory.setParent(this);
    }

    @Transient
    public String getImagePath() {
        if (this.id == null || this.image == null) return "/images/image-thumbnail.png";
        return "/category-images/" + this.id + "/" + this.image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getName() != null ? getName().equals(category.getName()) : category.getName() == null;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
