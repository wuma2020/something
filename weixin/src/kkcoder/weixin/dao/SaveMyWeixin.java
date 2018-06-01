package kkcoder.weixin.dao;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;

public class SaveMyWeixin {
	
		QueryRunner query ; 
		public SaveMyWeixin() {
			DataSource pool=C3p0Pool.getPool();
			 query=new QueryRunner(pool);
			 System.out.println("初始化query");
		}

	
	public  void saveMyWeixinMessage(Map<String,String> map){
		System.out.println("map:" + map.get("MsgId"));
		String sql = "insert into blog.weixin (ToUserName,FromUserName, CreateTime, MsgType, MsgId, Content) values(?,?,?,?,?,?)";
		Object[] params = {map.get("ToUserName"),map.get("FromUserName"),map.get("CreateTime"),map.get("MsgType"),map.get("MsgId"),map.get("Content")};
		try {
			query.update(sql,   params);
		} catch (SQLException e) {
			System.out.println("textMessage插入数据库错误");
			e.printStackTrace();
		}
		
	}
	
	
}
