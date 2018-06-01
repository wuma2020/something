package kkcoder.weixin.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0conf {
	
	private static  DataSource pool = new ComboPooledDataSource();//添加配置文件
	
	private static ThreadLocal<Connection> t1 = new ThreadLocal<>();
	 public static Connection getCon() throws SQLException{
	        Connection con =t1.get();
	        if(con==null){
	            con=pool.getConnection();
	            t1.set(con);
	        }
	        return con;
	    }
	    public static DataSource getPool() {
	        return pool;
	    }
	    public static ThreadLocal<Connection> getTl() {
	        return t1;
	    }
}
