package cn.kkcoder.havefun;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.SocketSecurityException;
import java.util.logging.ConsoleHandler;

/**
 * 
 * @author static-mkk 
 * @time 19 Mar 2018
 * @myGithub https://github.com/static-mkk
 *  
 *  菜鸡聊天超级微型  服务端
 */
public class TalkServer {

	public static void main(String[] args) {

		try {
			InetAddress localhost = InetAddress.getLocalHost();
			String IP = localhost.getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException("获取IP失败");
		} 
		
		try {
			ServerSocket ss = new ServerSocket(8899,1000,InetAddress.getLocalHost());
			
			while(true){
				Socket s =  ss.accept();
				String sName = s.getInetAddress().getHostName();
				String sIP = s.getInetAddress().getHostAddress();
				System.out.println("连接进来的IP为： " + sIP +"  主机名 ：" + sName );
				InputStream in = s.getInputStream();
				int len = in.available();
				byte[] b = new byte[len];
				in.read(b);
				
				System.out.println(new String(b));
				
				
				
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
