package cn.kkcoder.demo.Exception;

import cn.kkcoder.demo.enums.ResultEnums;

/**
 * 这里是自己写的异常类
 * 这里的GirlsExecption主要是用于在service层，抛出异常时，能够添加异常的code值，因为普通的Execption不具有传入code的构造器函数
 * 好处是，在handle层的异常捕获类中，利用resultResp类的相关的方法时，能够灵活的填入code值，异常值.
 * @author : static-mkk
 * @time   : 25 Jun 2018 <br/>
 *
 */
public class GirlsException  extends RuntimeException{

	private Integer code;

	
	
	
	public GirlsException(ResultEnums enums) {
		super(enums.getMsg());
		this.code = enums.getCode();
	}

	public Integer getCode() {
		return code;
	}

	
	
	
}
