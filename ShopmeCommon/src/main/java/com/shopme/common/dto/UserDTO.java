package com.shopme.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDTO {

    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String photos;
    private boolean enabled;
    private Set<RoleDTO> roles;
}
