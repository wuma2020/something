package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class K_Song {

	
	/**
	 * 1.通过一个人的主页，来获取该人的关注者的主页
	 * 
	 */
	public List<String> getMyFocus(String personurl) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		//1.使用htmlunit，获取webclient
		WebClient w = new WebClient();
		w.setJavaScriptTimeout(3000);
		w.getOptions().setJavaScriptEnabled(true);//设置支持javascript脚本   
        w.getOptions().setCssEnabled(false);//禁用css支持  
        w.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常  
        w.getOptions().setTimeout(100000);//设置连接超时时间  
        HtmlPage html = w.getPage(personurl);
        //2.获取我的主页面《关注》的a标签
        DomNodeList<DomElement> focusFriendsA =  html.getElementsByTagName("a");
        DomElement a_choosed = null;
        for(DomElement daoElement : focusFriendsA){
        	if(daoElement.getAttribute("class").equals("my_show__link j_get_follow") && 
        			daoElement.getAttribute("data-follow").equals("following")){
        		a_choosed = daoElement;
        	}
        }
        
        //3.点击，然后获取一个新的htmlpage页面
        HtmlPage  focusFriendsPage =  a_choosed.click();
        //4.获取关注的人的主页url
       Document jsopuFocusPage =  Jsoup.parse(focusFriendsPage.asXml());
       Element friendsUl = jsopuFocusPage.getElementById("following-list");
       Elements friendsLiList = friendsUl.select("li");
       List<String> friendsPersonUrlList = new ArrayList<>();
       for(Element e_one : friendsLiList){
    	   friendsPersonUrlList.add(e_one.select("a").attr("href"));
       }
        return friendsPersonUrlList;
        
	}

	/**
	 * 2.通过某一个人的主页，来获取该人的所有作品（歌曲）
	 * 
	 */
		public void getSongByPersonUrl(String personUrl) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException{
		//1.获取该person的htmlpage
		WebClient w = new WebClient();
		w.setJavaScriptTimeout(3000);
		w.getOptions().setJavaScriptEnabled(true);//设置支持javascript脚本   
        w.getOptions().setCssEnabled(false);//禁用css支持  
        w.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常  
        w.getOptions().setTimeout(100000);//设置连接超时时间  
        HtmlPage html = w.getPage(personUrl);
        
        //2.获取歌曲数量，加载全部的歌曲链接
        DomElement more =  html.getElementById("ugc");
        String moreNumber  = more.getTextContent();
        int index = moreNumber.indexOf(" ");
        moreNumber = moreNumber.substring(index+1);
        int songNumber = Integer.parseInt(moreNumber);
        int times = songNumber / 8 ;//需要点击加载更多的次数
        List<HtmlPage> pagelist = new ArrayList<>();//每次点击获取的新的htmlpage放到list里面
        pagelist.add(html);
        DomElement moreButtun = null ;
        if(times > 0){
        	for(int j =1 ; j <= times ; j++ ){
        		DomNodeList<DomElement> daoElements = html.getElementsByTagName("div");//获取所有div，然后遍历得到包含   查看更多的按钮的链接
        		if(moreButtun != null){
        			Thread.sleep(1000);
        			HtmlPage p = moreButtun.click();
        			pagelist .add(p);
        			continue;
        		}
        		for(DomElement daow : daoElements){
        			if(daow.getAttribute("data-more").equals("songs")){
        					moreButtun = daow.getFirstElementChild();
        				HtmlPage p = moreButtun.click();
        				pagelist.add(p);
        				break;
        			}
        		}
        	}
        }
        //-->获取歌曲数量，加载全部的歌曲链接结束
        //现在应该拥有了全部的歌曲内容
        
        //3.获取最全的htmlpage
       
        int pageSize = pagelist.size();
        HtmlPage lastPage = pagelist.get( pageSize - 1 );
        Document doc =  Jsoup.parse(lastPage.asXml());
		//4. 获取每一首歌的链接
        //System.out.println(doc.toString());
        Elements elements =  doc.select("li.mod_playlist__item");
        //如果有人一首歌都没有，下面不执行
        	if(!elements.isEmpty()){
		        for(Element e : elements){
					String e_a_href = e.select("a.mod_playlist__cover").attr("href");//获取了该歌曲的对应的播放页面的连接
					Map<String ,String> map = getOneSongInfo(e_a_href);
					String name = map.get("name");
					String href = map.get("src");
					//获取连接之后就下载该歌曲
					
					
					URL url = new URL(href);
					InputStream in = url.openStream();
					String realPath = "C:/KSONG/" + name + ".m4a" ;
					File f = new File(realPath);
					
					if(f.exists()){
						continue;
					}
					if(!f.exists()){
						f.getParentFile().mkdir();
						f.createNewFile();
					}
					FileOutputStream fout = new FileOutputStream(f);
					byte[] b = new byte[1024];
					int length = 0 ;
					while((length = in.read(b,0,1024)) >0){
						fout.write(b,0,length);//注意这里，一定要指定write的长度write(b,0,length)  
					}
					in.close();
					fout.close();		
		        }
        	}
	}
	
	/**
	 * 3.通过某一个歌的主页，来获取该歌的信息
	 */

	public Map<String,String> getOneSongInfo(String oneSongHref) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		
		final WebClient w = new WebClient();
		w.setJavaScriptTimeout(5000);  
        w.getOptions().setUseInsecureSSL(true);//接受任何主机连接 无论是否有有效证书  
        w.getOptions().setJavaScriptEnabled(true);//设置支持javascript脚本   
        w.getOptions().setCssEnabled(false);//禁用css支持  
        w.getOptions().setThrowExceptionOnScriptError(false);//js运行错误时不抛出异常  
        w.getOptions().setTimeout(100000);//设置连接超时时间  
        w.getOptions().setDoNotTrackEnabled(false);   
        
        final HtmlPage html = w.getPage(oneSongHref);
		DomElement element =  html.getElementById("player");
		String name = element.getAttribute("title");
		String src = element.getAttribute("src");
		System.out.println("KK" + name + src);
		Map<String ,String> map = new HashMap<>();
		map.put("name",name);
		map.put("src",src);
		w.close();
		return map;
	} 
	
	/**
	 * 4.通过某一个歌的具体的href，来下载该歌曲
	 */
	public void getSongByHref(String href,String name,String filePathParent) throws IOException{
		
		URL url = new URL(href);
		InputStream in = url.openStream();
		String realPath = filePathParent + name + ".m4a" ;
		File f = new File(realPath);
		
		if(!f.exists()){
			f.getParentFile().mkdir();
			f.createNewFile();
		}
		FileOutputStream fout = new FileOutputStream(f);
		byte[] b = new byte[1024];
		int length = 0 ;
		while((length = in.read(b,0,1024)) >0){
			fout.write(b,0,length);//注意这里，一定要指定write的长度write(b,0,length)  
		}
		in.close();
		fout.close();		
		
		
	}
	
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		List<String> fridensUrl =  new K_Song().getMyFocus("http://kg.qq.com/node/personal?uid=639a95832d29358d3d");
	
		for(String s : fridensUrl){
			new K_Song().getSongByPersonUrl(s);
		}
		
//		new K_Song().getSongByPersonUrl("http://node.kg.qq.com/personal?uid=639d95862d25318b&g_f=personal");
	}

}
