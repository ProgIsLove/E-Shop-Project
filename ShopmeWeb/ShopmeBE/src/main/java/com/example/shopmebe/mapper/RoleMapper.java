package com.example.shopmebe.mapper;

import com.shopme.common.dto.RoleDTO;
import com.shopme.common.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

//    @BeanMapping(builder = @Builder(disableBuilder = true))
    RoleDTO roleEntityToRoleDTO(Role role);
//    @BeanMapping(builder = @Builder(disableBuilder = true))
    Role roleDTOToRoleEntity(RoleDTO roleDTO);
}
