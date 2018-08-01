package cn.kkcoder.dao;

import cn.kkcoder.domain.User;

import java.util.List;

/**
 * 用户信息查询接口
 */
public interface UserDao {
    User getPasswordByUsername(String username);

    List<String> getRoleByUsername(String username);

    List<String> getPerssionsByUsername(String username);
}
