package com.huyendieu.parking.bootstrap;

import com.huyendieu.parking.constants.PermissionConstant;
import com.huyendieu.parking.entities.RoleUserEntity;
import com.huyendieu.parking.entities.UserEntity;
import com.huyendieu.parking.repositories.RoleUserRepository;
import com.huyendieu.parking.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Override
    public void run(String... args) throws Exception {

        // create admin role
        RoleUserEntity roleUserEntity = RoleUserEntity.builder()
                .id(new ObjectId())
                .code(PermissionConstant.RoleCode.ADMIN.getCode())
                .name(PermissionConstant.RoleCode.ADMIN.getName())
                .createdBy("")
                .createdDate("")
                .build();
        roleUserRepository.save(roleUserEntity);

        //add admin account
        UserEntity adminEntity = UserEntity.builder()
                .userName(PermissionConstant.RoleCode.ADMIN.getName())
                .password("$2a$10$f7RMh3epXApK615P84.VpO.ElgRgkBXwba1rph974t6ur6QfAtGZG") // password = secret123
                .roleUser(roleUserEntity)
                .build();
        userRepository.save(adminEntity);
    }
}
