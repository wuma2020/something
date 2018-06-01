package kkcoder.weixin.controller;

import java.util.Map;

import kkcoder.weixin.utils.MessageType;

public class EventDispatcher {

	/**事件处理，加工方法*/
	public static String processMessage(Map<String,String> map) {
		  if (map.get("Event").equals(MessageType.EVENT_TYPE_SUBSCRIBE)) { //关注事件
	            System.out.println("==============这是关注事件！");
	        }

	        if (map.get("Event").equals(MessageType.EVENT_TYPE_UNSUBSCRIBE)) { //取消关注事件
	            System.out.println("==============这是取消关注事件！");
	        }

	        if (map.get("Event").equals(MessageType.EVENT_TYPE_CLICK)) { //自定义菜单点击事件
	            System.out.println("==============这是自定义菜单点击事件！");
	        }


	        return "";
	    }
	}
