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

    public String getPhotosImagePath() {
        if (id == null || photos == null) return "/images/image-portrait-solid.svg";
        return "/user-photos/" + this.id + "/" + this.photos;
    }

    @Override
    public String toString() {
        return String.format("User [id= %d, email= %s, firstName= %s, lastName= %s, roles= %s]",
                id, email, firstName, lastName, roles);
    }
}
