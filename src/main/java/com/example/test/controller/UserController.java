package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.mapper.AppUserMapper;
import com.example.test.pojo.Appuser;
import com.example.test.pojo.User;
import com.example.test.service.RestfulServiceImpl;
import com.example.test.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Title:
 * Description:ribbon测试入口类
 * Copyright:
 * Company:
 * Project: SrpingBootTest
 * Create User: TRS-chen
 * Create Time:2018/8/3 18:24
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private RestfulServiceImpl restfulService;
    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier(value = "primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    @Autowired
    @Qualifier(value = "secondJdbcTemplate")
    private JdbcTemplate secondJdbcTemplate;

    @Autowired
    @Qualifier(value = "myCatJdbcTemplate")
    private JdbcTemplate myCatJdbcTemplate;

    @Autowired
    private AppUserMapper userMapper;

    @Value("${server.port}")
    private String serverPort;
    @Value("${spring.datasource.primary.jdbc-url}")
    private String jdbcUrl;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(5);

    int flag = 0;

    /**
     * 测试ribbon，消费者入口
     *
     * @param name
     * @return
     */
    @GetMapping("/hello/{name}")
    public String test(@PathVariable String name) {
        return "name:" + name + ",msg:" + restfulService.getRestData(name);
    }

    /**
     * 测试ribbon，消费者入口
     *
     * @return
     */
    @GetMapping("getListData")
    public String getListData() {
        return restfulService.getListData();
    }

    /**
     * 测试ribbon，测试数据库链接
     *
     * @return
     */
    @RequestMapping(value = "getDbData", method = RequestMethod.GET)
    public List<Map<String, Object>> getDbData() {
        String sql = "select * from appuser ";
        List<Map<String, Object>> list = primaryJdbcTemplate.queryForList(sql);
        return list;
    }

//    /**
//     * 测试BlockingQueue
//     *
//     * @return
//     */
//    @RequestMapping(value = "testBQ", method = RequestMethod.POST)
//    @ResponseBody
//    public String testBQ() {
//
//        new Thread(() ->product(blockingQueue)).start();
//
//        new Thread(() ->cunsumer(blockingQueue)).start();
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("success", true);
//        return jsonObject.toString();
//    }
//
//    private void product(BlockingQueue<String> blockingQueue){
//        for (int i = 0; i <30 ; i++) {
////            try {
////                Thread.sleep(400);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//            String userKey = UUID.randomUUID().toString().replace("-", "");
//            blockingQueue.offer(userKey);
//            System.out.println("--------size1:"+blockingQueue.size());
//        }
//
//    }
//
//    private void cunsumer(BlockingQueue<String> blockingQueue){
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < blockingQueue.size(); i++) {
//            String code = blockingQueue.poll();
//            System.out.println("---------:" + code + ",size:" + blockingQueue.size());
//        }
//    }

    @RequestMapping(value = "addAppuser2", method = RequestMethod.POST)
    @ResponseBody
    public String addAppuser2(String nickName) {
        String userKey = UUID.randomUUID().toString().replace("-", "");
        nickName = "\uD83C\uDF3C 妙盒子\uD83C\uDF3C";
        String insertSql = "INSERT appuser(nickName,userKey,create_date) VALUES('" + nickName + "','" + userKey + "',NOW()) ";
        int count = secondJdbcTemplate.update(insertSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        return jsonObject.toString();
    }

    /**
     * 测试ribbon，测试数据库插入
     *
     * @return
     */
    @RequestMapping(value = "addAppuser", method = RequestMethod.POST)
    @ResponseBody
    public String addAppuser(String nickName) {
        String userKey = UUID.randomUUID().toString().replace("-", "");
        String insertSql = "INSERT appuser(nickName,userKey,create_date) VALUES('" + nickName + "','" + userKey + "',NOW()) ";
        int count = primaryJdbcTemplate.update(insertSql);
        //updateInvite(userKey);
        blockingQueue.offer(userKey);
        new Thread(() -> updateInvite()).start();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        return jsonObject.toString();
    }

    /**
     * 测试mycat，测试数据库插入
     *
     * @return
     */
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(String userName, int counts) {
        int maxId = 1;
        String queryMaxId = "SELECT max(id) as maxId FROM user";
        Map<String, Object> map = myCatJdbcTemplate.queryForMap(queryMaxId);
        if (null != map && null != map.get("maxId")) {
            maxId = Integer.parseInt(String.valueOf(map.get("maxId"))) + 1;
        }
        if (0 == counts) {
            counts = 1000;
        }
        long start = System.currentTimeMillis();
        int insertCount = 0;
        for (int i = 0; i < counts; i++) {
            String insertSql = "insert into user(id,userName) value( " + maxId + ",'" + userName + "');";
            int count = myCatJdbcTemplate.update(insertSql);
            if (count > 0) {
                maxId++;
                insertCount++;
            }
        }
        long end = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", insertCount);
        jsonObject.put("time", (end - start));
        return jsonObject.toString();
    }

    /**
     * 测试事务
     * @param userName
     * @return
     */
    @RequestMapping(value = "addUserTran", method = RequestMethod.POST)
    @ResponseBody
    public String addUserTran(String userName) {
        boolean result = userService.addUserTranImpl(userName);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", result);
        return jsonObject.toString();
    }

    private void updateInvite() {
        if (blockingQueue.size() == 0) {
            return;
        }
        String userKey = blockingQueue.poll();
        boolean flag = true;
        int failCount = 0;  //计数器，防止出现异常情况进入死循环
        while (flag) {
            String queryIdSql = "SELECT inviteCode FROM appuser_invite WHERE `status`=0 LIMIT 1 ";
            Map<String, Object> dataMap = primaryJdbcTemplate.queryForMap(queryIdSql);
            int inviteCode = 0;
            if (null != dataMap && null != dataMap.get("inviteCode")) {
                inviteCode = Integer.parseInt(String.valueOf(dataMap.get("inviteCode")));
            }
            //实时更新数据库，先把当前这个inviteCode状态改为占用状态，
            String updateInviteSql = "UPDATE appuser_invite SET update_date=NOW(),`status`=1 WHERE `status`=0 AND inviteCode='" + inviteCode + "'";
            int updateInviteFlag = primaryJdbcTemplate.update(updateInviteSql);
            int updateFlag = 0;
            if (updateInviteFlag > 0) {
                String updateSql = "UPDATE appuser SET inviteCode='" + inviteCode + "' WHERE userKey='" + userKey + "' ";
                updateFlag = primaryJdbcTemplate.update(updateSql);
            } else {
                logger.error("[--实时更新数据库邀请码表异常--],关键字:userKey_" + userKey + ",inviteCode:" + inviteCode + ",updateInviteSql:" + updateInviteSql);
            }

            if (updateFlag > 0) {
                flag = false;
            }
            if (updateInviteFlag == 0 || updateFlag == 0) {
                failCount++;
            }
            if (failCount == 10) {
                logger.error("[--更新用户邀请码异常--],关键字:userKey_" + userKey);
                flag = false;
            }
        }
    }

    @RequestMapping(value = "createInvite", method = RequestMethod.POST)
    @ResponseBody
    public String createInvite() {
        int count = 5000;
        String queryMaxIdSql = "SELECT MAX(id) AS maxId FROM appuser_invite ";
        Map<String, Object> map = primaryJdbcTemplate.queryForMap(queryMaxIdSql);
        int maxId = 1;
        if (null != map) {
            maxId = null == map.get("maxId") ? 1 : Integer.parseInt(String.valueOf(map.get("maxId")));
        }
        long start = System.currentTimeMillis();
        int resultCount = 0;
        for (int i = 0; i < count; i++) {
            int inviteCode = getInviteCode(String.valueOf(maxId));
            String insertSql = "INSERT appuser_invite(inviteCode,`status`,create_date) VALUES(" + inviteCode + ",0,NOW())";

            int flag = primaryJdbcTemplate.update(insertSql);
            if (flag > 0) {
                resultCount++;
            }
            maxId++;
        }
        long end = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("totalCount", count);
        result.put("successCount", resultCount);
        result.put("time", (end - start));
        return result.toString();
    }

    /**
     * 根据规则要求不能出现4，由于保持数据一致性，故将4替换成5，下次重新创建会选取最大值作为基数.....
     *
     * @param id
     * @return
     */
    private static int getInviteCode(String id) {
        int incId = 0;
        if (id.length() <= 7) {
            incId = Integer.parseInt(id) + 10000000;
        } else {
            incId = Integer.parseInt(id);
        }

        while (isCuteIncId(incId) || String.valueOf(incId).contains("4")) {
            incId = getIncIdNew(incId);
        }
        return incId;
    }

    /**
     * 根据规则要求不能出现4，由于保持数据一致性，故将4替换成5，下次重新创建会选取最大值作为基数.....
     *
     * @param curIncId
     * @return
     */
    private static int getIncIdNew(int curIncId) {
        String incIdStr = String.valueOf(curIncId);
        incIdStr = incIdStr.replace("4", "5");
        return Integer.parseInt(incIdStr) + 1;
    }

    /**
     * 判断incId是否为靓号,预留一部分号码，包括代码中的和19500101-20201230之间的所有年份+月+日的邀请码（生日邀请码）
     *
     * @return
     */
    private static boolean isCuteIncId(int incId) {
        boolean result = false;
        //逻辑待定
        if (incId == 11111111 || incId == 22222222 || incId == 33333333 || incId == 55555555 || incId == 66666666 ||
                incId == 77777777 || incId == 88888888 || incId == 99999999 || incId == 12345678 || incId == 87654321 ||
                incId == 66668888 || incId == 88886666 || incId == 18888888 || incId == 16666666) {
            result = true;
        } else {
            String incIdStr = String.valueOf(incId);
            String startFour = incIdStr.substring(0, 4);
            String midTwo = incIdStr.substring(4, 6);
            String endTwo = incIdStr.substring(6, 8);
            if (Integer.parseInt(startFour) >= 1950 && Integer.parseInt(startFour) <= 2020 &&
                    Integer.parseInt(midTwo) >= 1 && Integer.parseInt(midTwo) <= 12 &&
                    Integer.parseInt(endTwo) >= 1 && Integer.parseInt(endTwo) <= 31) {  //增强逻辑，减少date的处理量
                String sourceDate = startFour + "-" + midTwo + "-" + endTwo;
                try {
                    Date date = format.parse(sourceDate);
                    result = true;
                } catch (ParseException e) {
                    result = false;
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "getStr", method = RequestMethod.POST)
    @ResponseBody
    public String getStr(@RequestParam(value = "code") String code) {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("------code" + code);
        return code + "-----";
    }

    /**
     * 登陆
     *
     * @param username 用户名
     * @param password 密码
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(String username, String password) {
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行认证登陆
        subject.login(token);
        //根据权限，指定返回数据
        //String role = userMapper.getRole(username);
        String querySql = "SELECT rule from rule a LEFT JOIN appuser b ON a.userId = b.id WHERE b.userName='" + username + "'";
        Map<String, Object> map = primaryJdbcTemplate.queryForMap(querySql);
        String role = null == map.get("rule") ? "" : String.valueOf(map.get("rule"));
        JSONObject jsonObject = new JSONObject();
        String msg = "";
        boolean success = false;
        String ssoId = "";
        if ("user".equals(role)) {
            msg = "登陆成功，user权限";
            success = true;
            ssoId = subject.getSession().getId().toString();
        }
        if ("admin".equals(role)) {
            msg = "登陆成功，admin权限";
            success = true;
            ssoId = subject.getSession().getId().toString();
        }
        jsonObject.put("success", success);
        jsonObject.put("msg", msg+",serverPort:"+serverPort+",jdbcUrl:"+jdbcUrl);
        jsonObject.put("ssoId", ssoId);
        return jsonObject.toString();
    }

    @RequestMapping(value = "testShardingAdd", method = RequestMethod.POST)
    @ResponseBody
    public String testShardingAdd() {
        int counts = 0;
        for (int i = 0; i < 10; i++) {
            String insertSql = "insert into t_order(id,orderId) value(" + i + "," + (i + 1) + ")";
            int count = secondJdbcTemplate.update(insertSql);
            if (count > 0) {
                counts++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", counts);
        return jsonObject.toString();
    }


    @RequestMapping(value = "getMethod", method = RequestMethod.POST)
    //@ResponseBody
    public String getMethod(@RequestBody User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("userName", user.getUserName());
        return jsonObject.toString();
    }

    @RequestMapping(value = "getAppuser", method = RequestMethod.GET)
    public String getMethod() {
        int count = userMapper.getAppuserCount();
        Appuser appuser = userMapper.getAppuser();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", appuser.getId());
        jsonObject.put("userName", appuser.getUserName());
        return jsonObject.toString();
    }
}
