package kkcoder.weixin.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class getConnectionToMSQL {

	public Connection getConnction(){
		
		Connection con = null;
		try {
			Class.forName(DBconf.JDBC_DRIVER);
			con = (Connection) DriverManager.getConnection(DBconf.DB_URL, DBconf.user,DBconf.password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("链接数据库失败");
			e.printStackTrace();
		}
		return con;		
	}
}
