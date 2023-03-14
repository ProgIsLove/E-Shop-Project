package com.example.shopmebe.mapper;

import com.shopme.common.dto.RoleDTO;
import com.shopme.common.dto.UserDTO;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

//    Role roleDTOToRoleEntity(RoleDTO roleDTO);

//    @BeanMapping(builder = @Builder(disableBuilder = true))
//    @Mapping(expression = "java(new HashSet<Role>(Arrays.asList(user.getRoles())))", target = "roles")

    User userDTOToUserEntity(UserDTO user);

    @Mapping(source = "roles", target = "roles")
    UserDTO userEntityToUserDTO(User user);

    List<UserDTO> usersEntityToUsersDTO(List<User> user);

//    @Named("roleSet")
//    default Set<RoleDTO> toSet(String text){
//        Set<RoleDTO> roles = new HashSet<>();
//        try {
//            roles = new ObjectMapper().readValue(text, new TypeReference<>() {
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return roles;
//    }

//    default Set<RoleDTO> toRole(String role) {
//        return role != null ? new HashSet<RoleDTO>().add(role) : null;
//    }

//    @BeanMapping(builder = @Builder(disableBuilder = true))
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    Role roleDTOToRoleEntity(RoleDTO role);

//    @Named("RoleId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    Role toRoleEntity(RoleDTO roleDTO);

//    @Named("roleDTOSet")
//    default Set<User> toRoleSet(Set<UserDTO> source) {
//        return source.stream().map(this::userDTOToUserEntity).collect(Collectors.toSet());
//    }

//    @AfterMapping
//    @BeanMapping(builder = @Builder(disableBuilder = true))
//    default void allRoles(UserDTO userDTO, @MappingTarget User user){
//        Set<RoleDTO> dtoRoles = userDTO.getRoles();
//
//        Set<Role> roles = new HashSet<>();
//
//        Role role = new Role();
//
//        if (dtoRoles != null) {
//            for (RoleDTO r : dtoRoles) {
//                role.setId(r.getId());
//                role.setName(r.getName());
//                role.setDescription(r.getDescription());
//                roles.add(role);
//            }
//            user.setRoles(roles);
////            user.setRoles(role);
//        }
//    }

//    @Named("RoleIdSet")
//    default Set<Role> roleToDTO(Set<RoleDTO> roleDTO) {
//        return roleDTO.stream().map(this::toRoleEntity).collect(Collectors.toSet());
//    }

}
