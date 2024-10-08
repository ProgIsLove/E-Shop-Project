package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categories")
@Setter
@Getter
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

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    @Transient
    private boolean hasChildren;

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name, String alias, String image, boolean enabled, Category parent, Set<Category> children) {
        this(id);
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enabled = enabled;
        this.parent = parent;
        this.children = children;
    }

    public Category(Integer id, String name, String alias) {
        this(id);
        this.name = name;
        this.alias = alias;
    }

    public static Category copyIdAndName(Category category) {
        Category copiedCategory = new Category();
        copiedCategory.setId(category.getId());
        copiedCategory.setName(category.getName());
        return copiedCategory;
    }

    public static Category copyFull(Category category) {
        Category copyCategory = copyIdAndName(category);
        copyCategory.setImage(category.getImage());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setHasChildren(!category.getChildren().isEmpty());

        return copyCategory;
    }

    public static Category copyFull(Category category, String name) {
        Category copyCategory = Category.copyFull(category);
        copyCategory.setName(name);
        return copyCategory;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "image-thumbnail.png";
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
        if (this.id == null) return "/images/image-thumbnail.png";
        return "/category-images/" + this.id + "/" + this.image;
    }


    //implement equals and hashCode for Category class when using it in a Set to ensure proper handling of duplicates.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getName() != null ? getName().equals(category.getName()) : category.getName() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
