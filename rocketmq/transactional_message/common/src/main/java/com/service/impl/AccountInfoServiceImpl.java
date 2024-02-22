package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dao.AccountInfoDao;
import com.model.AccountChangeEvent;
import com.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    /**
     * 向mq发送转账消息
     */
    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //将accountChangeEvent转成json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange", accountChangeEvent);
        String jsonString = jsonObject.toJSONString();
        //生成message类型
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        /*
          发送一条事务消息
          String txProducerGroup 生产组
          String destination topic，
          Message<?> message, 消息内容
          Object arg 参数
         */
        rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", "topic_txmsg", message, null);
    }

    /**
     * 更新账户，扣减金额
     */
    @Override
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        log.info("doUpdateAccountBalance,更新账户，扣减金额");
        //幂等判断
        if (accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return;
        }
        //扣减金额
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount() * -1);
        //添加事务日志
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        log.info("doUpdateAccountBalance,txNo:{}",accountChangeEvent.getTxNo());
        if (accountChangeEvent.getAmount() == 3) {
            throw new RuntimeException("人为制造异常");
        }
    }

    //更新账户，增加金额
    @Override
    @Transactional
    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        log.info("bank2更新本地账号，账号：{},金额：{}",accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo())>0){

            return ;
        }
        //增加金额
        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
        //添加事务记录，用于幂等
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        if(accountChangeEvent.getAmount() == 4){
            throw new RuntimeException("人为制造异常");
        }
    }
}
