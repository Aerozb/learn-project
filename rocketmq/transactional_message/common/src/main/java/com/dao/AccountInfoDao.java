package com.dao;

import com.entity.AccountInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountInfoDao {
    @Update("update account_info set account_balance=account_balance+#{amount} where account_no=#{accountNo}")
    int updateAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);


    @Select("select * from account_info  where account_no=#{accountNo}")
    AccountInfo findByIdAccountNo(@Param("accountNo") String accountNo);


    @Select("select count(1) from de_duplication where tx_no = #{txNo}")
    int isExistTx(String txNo);


    @Insert("insert into de_duplication values(#{txNo},now());")
    int addTx(String txNo);

}
