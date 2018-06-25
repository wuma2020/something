package cn.kkcoder.demo.handle;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.kkcoder.demo.Exception.GirlsException;
import cn.kkcoder.demo.domain.ResultResp;
import cn.kkcoder.demo.enums.ResultEnums;
import cn.kkcoder.demo.utils.ResultUtil;

/**
 * 异常的捕获
 * @ControllerAdvice 没有添加值的话会捕获所有的controller的异常
 * @author : static-mkk
 * @time   : 25 Jun 2018 <br/>
 *
 */
@ControllerAdvice
public class ExceptionHandle {

	@ExceptionHandler(value= Exception.class)
	@ResponseBody
	public ResultResp error(Exception e) {
		
		if(e instanceof GirlsException) {
			return ResultUtil.error(((GirlsException) e).getCode(), e.getMessage());
		}
		return ResultUtil.error(ResultEnums.UNKNOW_ERROR.getCode(), ResultEnums.UNKNOW_ERROR.getMsg());
	}
	
}
