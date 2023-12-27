package com.transaction_springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.transaction_springboot.entity.UserPoint;
import com.transaction_springboot.mapper.UserPointMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserPointService extends ServiceImpl<UserPointMapper, UserPoint> {

    public void add() {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(1);
        userPoint.setPoint(1);
       save(userPoint);
        int i = 5 / 0;
    }
}
