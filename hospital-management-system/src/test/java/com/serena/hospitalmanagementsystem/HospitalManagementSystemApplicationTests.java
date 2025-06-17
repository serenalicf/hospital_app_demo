package com.serena.hospitalmanagementsystem;

import com.serena.hospitalmanagementsystem.entity.User;
import com.serena.hospitalmanagementsystem.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HospitalManagementSystemApplicationTests {

    @Autowired
    private UserMapper userMapper;
    
    @Test
    void contextLoads() {
    }
    
    @Test
    public void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testAdd() {
        User user = new User();
        user.setName("lucy");
        user.setAge(20);
        user.setEmail("lucy@serena.com");

        int newUser = userMapper.insert(user); // no. of affected row
        System.out.println(newUser);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1934917755509784577L); //update user set name = "Lucy Mary" where id = 1934917755509784577
        user.setName("Lucy Mary");
        int newUser =  userMapper.updateById(user);
        System.out.println(newUser);
    }




}
