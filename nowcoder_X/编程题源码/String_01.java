package cn.kkcoder.jianzhi_offer;


/**
 *
 * 请实现一个函数，将一个字符串中的空格替换成“%20”。
 * 例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 *
 */

/**
 * Created by static-mkk on 4/4/2018.
 */
public class String_01 {
	public static void main(String[] args) {
		String result = replaceSpace(new StringBuffer("aaa ss  ss  s aa "));
		System.out.println(result);
	}

	private static String replaceSpace(StringBuffer str) {
		String s = str.toString();

		//注释为第二种方法
//		StringBuffer sb = new StringBuffer();
//		char[] sChar = s.toCharArray();
//		for (char c:sChar){
//			if(c==' '){
//				sb.append("%20");
//			}else {
//				sb.append(c);
//			}
//
//		}
//		return str.toString().replaceAll(" ","%20");


		s = s.replaceAll(" ","%20");
		return s;

	}


}
