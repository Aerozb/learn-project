package com.transaction_springboot;

import com.transaction_springboot.entity.User;
import com.transaction_springboot.service.UserPointService;
import com.transaction_springboot.service.UserService;
import com.transaction_springboot.service.UserService2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionSpringbootApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserService2 userService2;
    @Autowired
    private UserPointService userPointService;

    /**
     * 不生效
     * 没有带事务注解的方法调带事务注解的方法
     */
    @Test
    void test1() {
        User user = new User();
        user.setId(1);
        user.setName("1");
        userService.initUser1(user);
    }

    /**
     * 生效
     * 带事务注解的方法调无事务注解的私有方法
     */
    @Test
    void test2() {
        User user = new User();
        user.setId(2);
        user.setName("2");
        userService.initUser2(user);
    }

    /**
     * 不生效
     * 捕获异常，不抛出
     */
    @Test
    void test3() {
        User user = new User();
        user.setId(3);
        user.setName("3");
        userService.initUser3(user);
    }

    /**
     * 不生效
     * 不带事务方法调带事务方法
     */
    @Test
    void test4() {
        User user = new User();
        user.setId(4);
        user.setName("4");
        userService.initUser4(user);
    }

    /**
     * 生效
     *
     * @Transactional写在类上，公有调用私有
     */
    @Test
    void test6() {
        User user = new User();
        user.setId(6);
        user.setName("6");
        userService2.initUser6(user);
    }

    /**
     * 生效
     * 模拟控制层调用带事务的方法，用try catch捕获异常，事务是否生效
     * 先输出1在输出2
     */
    @Test
    void test7() {
        boolean flag = false;
        try {
            userPointService.add();
        } catch (Exception e) {
            System.out.println(1);
            flag = false;
        } finally {
            if (!flag) {
                System.out.println(2);
            }
        }
    }

}
