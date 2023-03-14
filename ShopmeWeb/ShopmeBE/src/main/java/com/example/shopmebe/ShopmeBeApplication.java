package com.example.shopmebe;

import com.example.shopmebe.exception.UserNotFoundException;
import com.example.shopmebe.mapper.UserMapper;
import com.example.shopmebe.repository.UserRepository;
import com.shopme.common.dto.RoleDTO;
import com.shopme.common.dto.UserDTO;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.shopme.common.dto"})
public class ShopmeBeApplication {

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBeApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Role r = new Role();
//        r.setId(1);
//        r.setName("Admin");
//        r.setDescription("...");
//        Set<Role> roles = new HashSet<>();
//        roles.add(r);
//        User u1 = new User();
//        u1.setId(1);
//        u1.setFirstName("Pepa");
//        u1.setLastName("Dvorak");
//        u1.setEmail("pepa@google.com");
//        u1.setPassword("jflksjfklsjflk");
//        u1.setRoles(roles);
//        userRepository.save(u1);
//
//        UserDTO userDTO = userMapper.userEntityToUserDTO(userRepository.findById(1)
//                                                                       .orElseThrow(() -> new UserNotFoundException(String.format("Could not find any user with ID %d", 1))));
//        System.out.println(userDTO);
//    }
}
