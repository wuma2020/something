package cn.kkcoder.demo.enums;


/**
 * 这里添加所有的异常的枚举类型，统一管理.
 * @author : static-mkk
 * @time   : 25 Jun 2018 <br/>
 *
 */
public enum ResultEnums {
	
	UNKNOW_ERROR(-1,"未知错误"),
	SUCCESS(0,"成功"),
	SMALL(100,"你还太小"),
	MIDDLE(101,"你19了")
	;
	private Integer code;
	private String msg;
	
	ResultEnums(Integer code,String msg) {
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	
	
}
