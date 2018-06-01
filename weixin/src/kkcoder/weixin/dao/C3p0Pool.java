package kkcoder.weixin.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0Pool {

	 private  static DataSource pool=new ComboPooledDataSource();//读取配置文件
	    private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();
	    public static Connection getCon() throws SQLException{
	        Connection con =tl.get();
	        if(con==null){
	            con=pool.getConnection();
	            tl.set(con);
	        }
	        return con;
	    }
	    public static DataSource getPool() {
	        return pool;
	    }
	    public static ThreadLocal<Connection> getTl() {
	        return tl;
	    }
	
}
