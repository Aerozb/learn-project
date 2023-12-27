package com.transaction_springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transaction_springboot.entity.User;
import com.transaction_springboot.entity.UserPoint;
import com.transaction_springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserPointService userPointService;

    /* 开始 没有带事务注解的方法调带事务注解的方法:不生效 */
    public void initUser1(User user) {
        save(user);
        initPoint1(user.getId());
    }

    @Transactional
    public void initPoint1(Integer userId) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(0);
        userPointService.save(userPoint);
        int i = 5 / 0;
    }
    /* 结束 没有带事务注解的方法调带事务注解的方法 */


    /* 开始 带事务注解的方法调无事务注解的私有(公有)方法:生效 */
    @Transactional
    public void initUser2(User user) {
        save(user);
        initPoint2(user.getId());
    }

    private void initPoint2(Integer userId) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(0);
        userPointService.save(userPoint);
        int i = 5 / 0;
    }
    /* 结束 带事务注解的方法调无事务注解的私有(公有)方法 */


    /* 开始 捕获异常，不抛出:不生效 */
    @Transactional
    public void initUser3(User user) {
        save(user);
        initPoint3(user.getId());
    }

    public void initPoint3(Integer userId) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(0);
        userPointService.save(userPoint);
        try {
            int i = 5 / 0;
        } catch (Exception e) {
            System.out.println("不抛出异常，事务不生效");
        }
    }
    /* 结束 捕获异常，不抛出:不生效 */


    /* 开始 不带事务方法调带事务方法:不生效 */
    public void initUser4(User user) {
        save(user);
        initUser4_1(user);
    }

    @Transactional
    public void initUser4_1(User user) {
        user.setId(user.getId()+1);
        save(user);
        int i = 5 / 0;
    }
    /* 结束 不带事务方法调带事务方法:不生效 */


}
