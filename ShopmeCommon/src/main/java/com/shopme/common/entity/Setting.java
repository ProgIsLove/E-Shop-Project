package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "settings")
@Getter
@Setter
public class Setting {

    @Id
    @Column(name="`key`" ,nullable = false, length = 128)
    private String key;

    @Column(nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private SettingsCategory category;

    public Setting() {
    }

    public Setting(String key) {
        this.key = key;
    }

    public Setting(String key, String value, SettingsCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
}
