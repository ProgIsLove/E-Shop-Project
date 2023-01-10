package com.example.shopmebe.mapper;

import com.shopme.common.dto.UserDTO;
import com.shopme.common.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userEntityToUserDTO(User user);

}
