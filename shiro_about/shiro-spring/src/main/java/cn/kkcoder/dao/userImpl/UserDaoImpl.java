package cn.kkcoder.dao.userImpl;

import cn.kkcoder.dao.UserDao;
import cn.kkcoder.domain.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *  UserDao实现类
 */
@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public User getPasswordByUsername(String username) {
        String sql = "select username , password from shiro.t_shiro where username = ? ";
        List<User> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
        //判断集合是否为null
        if(CollectionUtils.isEmpty(list)){
            return  null;
        }
        return list.get(0);
    }

    /**
     * 根据username从数据库中获取角色信息
     * @param username
     * @return
     */
    public List<String> getRoleByUsername(String username) {
        String sql = "select username, t_roles, t_permission from t_shiro_permission where username = ? ";
         List<String> rolesList = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String rolesString = resultSet.getString("t_roles");
                return rolesString;
            }
        });
        return rolesList;
    }

    /**
     * 根据username从数据库中获取权限数据
     * @param username
     * @return
     */
    public List<String> getPerssionsByUsername(String username) {
        String sql = "select  t_permission from t_shiro_permission where username = ? ";
        List<String> permissionList = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String permissionString = resultSet.getString("t_permission");
                return permissionString;
            }
        });
        return permissionList;
    }
}
