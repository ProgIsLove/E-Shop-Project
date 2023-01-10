package com.example.shopmebe.mapper;

import com.shopme.common.dto.RoleDTO;
import com.shopme.common.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO roleEntityToRoleDTO(Role role);
}
