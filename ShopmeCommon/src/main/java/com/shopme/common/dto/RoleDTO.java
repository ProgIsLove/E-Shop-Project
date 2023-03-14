package com.shopme.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoleDTO {

    private Integer id;
    private String name;
    private String description;

    @Override
    public String toString() {
        return this.name;
    }
}
