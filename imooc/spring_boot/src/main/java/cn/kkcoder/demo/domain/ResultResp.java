package cn.kkcoder.demo.domain;


/**
 * 这个是返回给浏览器的信息的统一格式的实体类
 * @author : static-mkk
 * @time   : 25 Jun 2018 <br/>
 *
 */
public class ResultResp {

	
	
	
	/**错误码*/
	private Integer code;
	/**提示信息*/
	private String msg;
	/**数据*/
	private Object Data;

	public ResultResp() {
		super();
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return Data;
	}
	public void setData(Object data) {
		Data = data;
	}
	
	
	
}
