package com.transaction_springboot;

import com.transaction_springboot.entity.User;
import com.transaction_springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TransactionSpringbootApplicationTests {

    @Autowired
    private UserService userService;

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
     * 这个是生效的
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
	 * 捕获异常，不抛出
	 */
	@Test
	void test4() {
		User user = new User();
		user.setId(3);
		user.setName("3");
		userService.initUser3(user);
	}

}
