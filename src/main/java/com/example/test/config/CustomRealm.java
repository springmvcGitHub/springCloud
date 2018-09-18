package com.example.test.config;

import com.example.test.pojo.Appuser;
import com.mysql.jdbc.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    @Qualifier(value = "primaryJdbcTemplate")
    private JdbcTemplate primaryJdbcTemplate;

    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 从数据库获取对应用户名密码的用户
        String queryPwdSql = "SELECT password FROM appuser WHERE userName='" + token.getUsername() + "'";
        List<Map<String,Object>> appuserList = primaryJdbcTemplate.queryForList(queryPwdSql);//primaryJdbcTemplate.queryForList(queryPwdSql, Appuser.class);
        if(null == appuserList || appuserList.size() == 0){
            throw new AccountException("用户名不正确");
        }
        Map<String,Object> map = appuserList.get(appuserList.size()-1);
        String password = null == map || null == map.get("password") ? null : String.valueOf(map.get("password"));
        //String password = userMapper.getPassword(token.getUsername());
        if (null == password) {
            throw new AccountException("用户名不正确");
        } else if (!password.equals(new String((char[]) token.getCredentials()))) {
            throw new AccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
    }

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("————权限认证————");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        //String role = userMapper.getRole(username);
        String querySql = "SELECT rule from rule a LEFT JOIN appuser b ON a.userId = b.id WHERE b.userName='" + username + "'";
        List<Map<String, Object>> list = primaryJdbcTemplate.queryForList(querySql);
        Set<String> set = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String,Object> map = list.get(i);
            String role = null == map.get("rule") ? "" : String.valueOf(map.get("rule"));
            //需要将 role 封装到 Set 作为 info.setRoles() 的参数
            set.add(role);
        }

        //设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }
}
