package com.example.shopmebe.service;

import com.example.shopmebe.mapper.RoleMapper;
import com.example.shopmebe.repository.RoleRepository;
import com.shopme.common.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RoleService {

    private RoleRepository roleRepository;
    private RoleMapper roleMapper;

//    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
//        this.roleRepository = roleRepository;
//        this.roleMapper = roleMapper;
//    }

    public List<Role> listRoles() {

        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);

        return roles;
    }
}
