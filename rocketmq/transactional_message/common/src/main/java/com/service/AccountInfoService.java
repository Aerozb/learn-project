package com.service;

import com.model.AccountChangeEvent;
import org.springframework.transaction.annotation.Transactional;

public interface AccountInfoService {

    //向mq发送转账消息
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);
    //更新账户，扣减金额
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    //更新账户，增加金额
    @Transactional
    void addAccountInfoBalance(AccountChangeEvent accountChangeEvent);
}
