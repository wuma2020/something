package kkcoder.weixin.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import kkcoder.weixin.domain.resp.Article;
import kkcoder.weixin.domain.resp.ImageMessage;
import kkcoder.weixin.domain.resp.MusicMessage;
import kkcoder.weixin.domain.resp.NewsMessage;
import kkcoder.weixin.domain.resp.TextMessage;
import kkcoder.weixin.domain.resp.VideoMessage;
import kkcoder.weixin.domain.resp.VoiceMessage;

public class MessageUtil {

	/**
	 *  处理dopost里面request里面的微信转发过来的xml
	 * @param request 传进来的request请求
	 * @return  返回map，存放了xml中的信息
	 */
	public static Map<String,String> parseXml(HttpServletRequest request){
		Map<String,String> map = new HashMap<String, String>();
		
		InputStream inputStream = null;
		try {
		inputStream = request.getInputStream();
		} catch (IOException e) {
			System.out.println("MessageUtil.parseXml()中request.getInputStream();方法失败");
			e.printStackTrace();
		}
		/*利用dom4j的SAXReader来解析xml*/
		SAXReader reader = new SAXReader();
		
		/*获取输入流里的xml元素*/
		Document document = null;
		try {
		document  =  reader.read(inputStream);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*获取root节点下的所有的子节点*/
		List<Element> elementList  =  document.getRootElement().elements();
		
		/*下次用iterator来写，for效率差*/
		
		for(Element e : elementList){
			map.put(e.getName(), e.getText());
			System.out.println("xml内容是：" + e.getName() + " : "+ e.getText() );
		}
		try {
			inputStream.close();
		} catch (IOException e1) {
			System.out.println("MessageUtil.parseXml()中inputStream.close();;方法失败");
			e1.printStackTrace();
		}
		
		return map;
	}
	
	
	/**获取xstream对象实例*/
	@SuppressWarnings("unused")
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有 xml 节点的转换都增加 CDATA 标记   
                boolean cdata = true;  
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                }  

                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });  
	
	/**
	 * 通过xstream把textmessage转换成xml的string对象并返回*
	 * @param txt 需要转成xml的文本消息
	 * @return
	 */
	public static String textMessageToXml(TextMessage txt){
		xstream.alias("xml",txt.getClass());
		return xstream.toXML(txt);
	}
	
	/**
	 * 通过xstream把newsMessage转换成xml的string对象并返回*
	 * @param newsMessage  需要转成xml的文本消息
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }

	/**
	 *  * 通过xstream把imageMessage转换成xml的string对象并返回*
	 * @param imageMessage 需要转成xml的文本消息
	 * @return
	 */
    public static String imageMessageToXml(ImageMessage imageMessage) {
        xstream.alias("xml", imageMessage.getClass());
        return xstream.toXML(imageMessage);
    }

	/**
	 *  * 通过xstream把newsMessage转换成xml的VoiceMessage对象并返回*
	 * @param voiceMessage 需要转成xml的文本消息
	 * @return
	 */
    public static String voiceMessageToXml(VoiceMessage voiceMessage) {
        xstream.alias("xml", voiceMessage.getClass());
        return xstream.toXML(voiceMessage);
    }

	/**
	 *  * 通过xstream把videoMessage转换成xml的string对象并返回*
	 * @param videoMessage 需要转成xml的文本消息
	 * @return
	 */
    public static String videoMessageToXml(VideoMessage videoMessage) {
        xstream.alias("xml", videoMessage.getClass());
        return xstream.toXML(videoMessage);
    }

	/**
	 *  * 通过xstream把musicMessage转换成xml的string对象并返回*
	 * @param musicMessage 需要转成xml的文本消息
	 * @return
	 */
    public static String musicMessageToXml(MusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

	
	
}
