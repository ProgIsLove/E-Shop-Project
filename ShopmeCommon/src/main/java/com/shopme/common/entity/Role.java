package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 40, nullable = false, unique = true)
    private String name;
    @Column(length = 150, nullable = false)
    private String description;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Role role = (Role) obj;
        return getName() != null ? getName().equals(role.getName()) : role.getName() == null;
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
