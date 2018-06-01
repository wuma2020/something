package kkcoder.weixin.controller;

import java.util.Date;
import java.util.Map;

import kkcoder.weixin.domain.resp.TextMessage;
import kkcoder.weixin.utils.MessageType;
import kkcoder.weixin.utils.MessageUtil;

public class MsgDispatcher {
	/**消息处理，加工方法*/
	public  static String processMessage(Map<String,String> map) {
		if(map.get("MsgType").equals(MessageType.REQ_MESSAGE_TYPE_TEXT)){//文本消息
			System.out.println("======文本消息");
			String openid = map.get("FromUserName");
			String myid = map.get("ToUserName");
			
			//设置自动返回消息的内容
			TextMessage txtmsg = new TextMessage();
			txtmsg.setToUserName(openid);
			txtmsg.setFromUserName(myid);
			txtmsg.setCreateTime(new Date().getTime());
			txtmsg.setMsgType(MessageType.RESP_MESSAGE_TYPE_TEXT);
			
			txtmsg.setContent("success");//必须回复"success"或者""空字符串，不然会微信会以为服务器出错。
			String xml =  MessageUtil.textMessageToXml(txtmsg);
			return xml;
		}

		if(map.get("MsgType").equals(MessageType.REQ_MESSAGE_TYPE_IMAGE)){//image消息
			System.out.println("======图像消息");
		}

		if(map.get("MsgType").equals(MessageType.REQ_MESSAGE_TYPE_LINK)){//链接消息
			System.out.println("======链接消息");
		}

		if(map.get("MsgType").equals(MessageType.REQ_MESSAGE_TYPE_LOCATION)){//位置消息
			System.out.println("======位置消息");
		}
		
		if(map.get("MsgType").equals(MessageType.REQ_MESSAGE_TYPE_VOICE)){//语音消息
			System.out.println("======语音消息");
		}
		return "";
	}
}
