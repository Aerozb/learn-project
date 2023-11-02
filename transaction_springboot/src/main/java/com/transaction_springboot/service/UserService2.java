package com.transaction_springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transaction_springboot.entity.User;
import com.transaction_springboot.entity.UserPoint;
import com.transaction_springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService2 extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserPointService userPointService;

    public void initUser6(User user) {
        save(user);
        initPoint6(user.getId());
    }

    private void initPoint6(Integer userId) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(0);
        userPointService.save(userPoint);
        int i = 6 / 0;
    }

}
