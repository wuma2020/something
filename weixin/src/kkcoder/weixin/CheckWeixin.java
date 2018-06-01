package kkcoder.weixin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kkcoder.weixin.controller.EventDispatcher;
import kkcoder.weixin.controller.MsgDispatcher;
import kkcoder.weixin.dao.SaveMyWeixin;
import kkcoder.weixin.utils.MessageType;
import kkcoder.weixin.utils.MessageUtil;
public class CheckWeixin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CheckWeixin() {
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		//验证微信服务器转发过来的信息
		String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
		
        
        PrintWriter out = response.getWriter();
	        if(CheckUtil.checkSignature(signature, timestamp, nonce)){
	            out.print(echostr);
	        }else{
	        	 out.print("");
	        }
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		Map<String,String> map = MessageUtil.parseXml(request);
		String mesType = map.get("MsgType");
		/**根据消息类型，做相应处理*/
		if(mesType.equals(MessageType.RESP_MESSAGE_TYPE_EVENT)){
			String xml = EventDispatcher.processMessage(map);
			response.getWriter().write(xml);
		}else{
			String xml = MsgDispatcher.processMessage(map);
			response.getWriter().write("success");//不处理文本信息
			/**存储用户发送信息到数据库*/
			new SaveMyWeixin().saveMyWeixinMessage(map);
		}
		
	}

}
