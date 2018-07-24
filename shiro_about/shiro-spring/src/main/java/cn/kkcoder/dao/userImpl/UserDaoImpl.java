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
import java.util.Collections;
import java.util.List;

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
}
