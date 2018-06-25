package cn.kkcoder.demo.utils;

import cn.kkcoder.demo.domain.ResultResp;
import cn.kkcoder.demo.enums.ResultEnums;

/**
 * 这是处理统一返回结果的方法
 * @author : static-mkk
 * @time   : 25 Jun 2018 <br/>
 *
 */
public class ResultUtil {

	/**
	 * 错误信息的方法
	 * @param code 错误码
	 * @param msg  错误信息
	 * @return
	 */
	public static ResultResp error(Integer code,String msg) {
		ResultResp resp = new ResultResp();
		resp.setCode(code);
		resp.setMsg(msg);
		return resp;
	}
	
	/**
	 * 成功时返回的信息
	 * @param obj 数据内容
	 * @return
	 */
	public static ResultResp success(Object obj) {
		ResultResp resp = new ResultResp();
		resp.setCode(ResultEnums.SUCCESS.getCode());
		resp.setMsg(ResultEnums.SUCCESS.getMsg());
		resp.setData(obj);
		return resp;
	}
	
	
	public static ResultResp success() {
		return success(null);
	}
	
	
}
