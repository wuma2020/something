package cn.kkcoder.dao;

import cn.kkcoder.domain.User;

/**
 * 用户信息查询接口
 */
public interface UserDao {
    User getPasswordByUsername(String username);
}
