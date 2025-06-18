package com.serena.hospitalmanagementsystem;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.serena.hospitalmanagementsystem.entity.User;
import com.serena.hospitalmanagementsystem.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        user.setName("Derick");
        user.setAge(28);
        user.setEmail("derick@serena.com");

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


    //test optimistic lock
    @Test
    public void testOptimisticLock() {
        //query by id
        User user = userMapper.selectById(1935240912540024833L);
        //update
        user.setName("Ben");
        userMapper.updateById(user);
    }


    @Test
    public void testSelectByBatchIds() {
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(1, 2));
        System.out.println(userList);
    }

    @Test
    public void testSelectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Matthew");
        map.put("age", 22);
        List<User> userList = userMapper.selectByMap(map);
        System.out.println(userList);
    }

    //test pagination
    @Test
    public void testSelectByPage() {
        Page<User> page = new Page<>(1, 5);
        Page<User> userPage = userMapper.selectPage(page, null);
        long totalPages = userPage.getPages();
        long currentPage = userPage.getCurrent();
        List<User> userList = userPage.getRecords();
        long totalRecords = userPage.getTotal();
        boolean hasNext = userPage.hasNext();
        boolean hasPrevious = userPage.hasPrevious();
        System.out.println(totalPages);
        System.out.println(currentPage);
        System.out.println(userList);
        System.out.println(totalRecords);
        System.out.println(hasNext);
        System.out.println(hasPrevious);
    }

    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(1935252631840739329L);
        System.out.println(result);
    }

    @Test
    public void testQueryWrapper() {
        //ge / le / lt /gt
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 21);
        List<User> userList = userMapper.selectList(queryWrapper);
        System.out.println(userList);
    }

}
