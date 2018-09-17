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

    @Transactional(isolation = Isolation.READ_COMMITTED, value = "primaryTransactionManager")
    public boolean addUserTranImpl(String nickName) {
        int count = 0;
        String userKey = UUID.randomUUID().toString().replace("-", "");
        count = addUser(nickName, userKey);
        int count2 = addUser2(nickName, userKey);
//        if (true) {
//            throw new RuntimeException("111");
//        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    private int addUser(String nickName, String userKey) {
        String insertSql = "INSERT test2.appuser(nickName,userKey,create_date) VALUES('" + nickName + "','" + userKey + "',NOW()) ";
        int count = primaryJdbcTemplate.update(insertSql);
        String insertSql2 = "INSERT appuser_invite(inviteCode,`status`,create_date) VALUES('a1',0,NOW())";
        int count2 = primaryJdbcTemplate.update(insertSql2);
//        String querySql = "SELECT count(*) as count FROM appuser ";
//        Map<String, Object> userMap = primaryJdbcTemplate.queryForMap(querySql);//queryForList(querySql, Appuser.class);
//        int userCount = Integer.parseInt(userMap.get("count").toString());
        if (true) {
            throw new RuntimeException("111");
        }
        return 1;
    }

    private int addUser2(String nickName, String userKey) {
        String insertSql = "INSERT test2.appuser(nickName,userKey,create_date) VALUES('" + nickName + "','" + userKey + "',NOW()) ";
        int count = primaryJdbcTemplate.update(insertSql);
//        String insertSql2 = "INSERT appuser_invite(inviteCode,`status`,create_date) VALUES('a1',0,NOW())";
//        int count2 = primaryJdbcTemplate.update(insertSql2);
        return 1;
    }
}
