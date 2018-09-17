package com.example.test.service;

import com.example.test.pojo.Appuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    @Qualifier(value = "primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED,value = "primaryTransactionManager")
    public boolean addUserTranImpl(String nickName){
        int count = 0;
//        try {
            String userKey = UUID.randomUUID().toString().replace("-", "");
            //nickName = "\uD83C\uDF3C 妙盒子\uD83C\uDF3C";
            count = addUser(nickName,userKey);
//        }catch (Exception e){
//            e.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw e;
//        }
        if(count>0){
            return true;
        }else{
            return false;
        }
    }
    private int addUser(String nickName,String userKey){
        String insertSql = "INSERT appuser(nickName,userKey,create_date) VALUES('" + nickName + "','" + userKey + "',NOW()) ";
        int count =  primaryJdbcTemplate.update(insertSql);
        count =  primaryJdbcTemplate.update(insertSql);
        String querySql = "SELECT count(*) as count FROM appuser ";
        Map<String,Object> userMap = primaryJdbcTemplate.queryForMap(querySql);//queryForList(querySql, Appuser.class);
        int userCount = Integer.parseInt(userMap.get("count").toString());
        if(true) {
            throw new RuntimeException("111");
        }
        return 1;
    }
}
