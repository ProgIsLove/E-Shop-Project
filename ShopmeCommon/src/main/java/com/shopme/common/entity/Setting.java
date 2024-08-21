package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    public Setting(String key, String value, SettingsCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }
}
