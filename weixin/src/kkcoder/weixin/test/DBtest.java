package kkcoder.weixin.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Test;

public class DBtest {

	
	@Test
	public void dbUtils(){
		//获取C3p0conf类中的DataSource，作为queryrunner的构造函数的参数
		DataSource ds = new C3p0conf().getPool();
		//创建queryrunner实例
		QueryRunner query  = new QueryRunner(ds); 
		
		/**下面介绍主要的几个dbutils的方法*/
		String sql = "select * from blog.weixin";
		//QueryRunner.query(Connection conn, String sql, ResultSetHandler<T> rsh)
		try {
			query.query(sql, new MapListHandler(), new Object(){});
		} catch (SQLException e) {
			System.out.println("query.query()方法失败1");
			e.printStackTrace();
		}
		
	}
	
}
