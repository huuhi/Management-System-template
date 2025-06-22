package DAO.dao;

import DAO.domain.User;

/**
 * @author 胡志坚
 * @version 1.0
 * 创造日期 2025/6/19
 * 说明:
 */
public class UserDao extends BasicDao<User>{
//    用户登录校验
    public User verifyUser(String username,String password){
        String sql="select id,username,role from user where username=? and password=md5(?);";
        User user = querySingle(sql, User.class, username, password);
        return user;
    }
//    用户注册
    public Boolean registerUser(String username,String password){
        String sql="insert into user(username,password) values(?,md5(?));";
        int update = update(sql, username, password);
        return update>0;
    }


}
