package cn.kkcoder.havefun;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Talk {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Socket s = null;
		try {
			s = new Socket("172.30.75.108", 8899);
		} catch (IOException e) {
			throw new RuntimeException("链接服务器失败.");
		}
		OutputStream out =null;
		try {
			out = s.getOutputStream();
			out.write("我是 客户端".getBytes());
		} catch (IOException e) {
			throw new RuntimeException("写出数据失败.");
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}

}
