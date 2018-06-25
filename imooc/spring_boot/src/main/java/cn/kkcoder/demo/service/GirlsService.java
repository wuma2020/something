package cn.kkcoder.demo.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.kkcoder.demo.Exception.GirlsException;
import cn.kkcoder.demo.dao.GirlsInterface;
import cn.kkcoder.demo.domain.Girls;
import cn.kkcoder.demo.domain.ResultResp;
import cn.kkcoder.demo.enums.ResultEnums;
import cn.kkcoder.demo.utils.ResultUtil;

/**
 * girls的业务层
 * @author : static-mkk
 * @time   : 24 Jun 2018 <br/>
 *
 */
@Service
public class GirlsService {

	@Autowired
	private GirlsInterface girlsInterface;
	
	/**
	 * @Transctional 注解表明该方法是事务提交
	 */
	@Transactional
	public void insertTwoGirls() {
	
		Girls g1 = new Girls();
		g1.setAge(221);
		g1.setName("g1!!");
		Girls g2 = new Girls();
		g2.setAge(111);
		g2.setName("g2");
		
		girlsInterface.save(g1);
		girlsInterface.save(g2);
	}

	/**
	 * 根据 id 获取一个女生信息
	 * @param id
	 * @return
	 */
	public ResultResp getAge(Integer id)  throws Exception{
		Girls g = girlsInterface.findOne(id);
		Integer age = g.getAge();
		//这里对年龄做判断
		if(age < 18) {
			throw new GirlsException(ResultEnums.SMALL);
		}else if(age >=18 && age < 20){
			throw new GirlsException(ResultEnums.MIDDLE);
		}
		
		return ResultUtil.success(g);
	}
	
}
