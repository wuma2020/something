package cn.kkcoder.demo.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationProviderResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.kkcoder.demo.dao.GirlsInterface;
import cn.kkcoder.demo.domain.Girls;
import cn.kkcoder.demo.domain.ResultResp;
import cn.kkcoder.demo.service.GirlsService;
import cn.kkcoder.demo.utils.ResultUtil;

/**
 * 操作表girls的控制器
 * @author : static-mkk
 * @time   : 24 Jun 2018 <br/>
 *
 */
@RestController
public class GirlsController {

	//相当于dao层对象实例
	@Autowired
	private GirlsInterface girlsInterface;
	
	
	@Autowired
	private GirlsService girlsService;
	
	
	/**
	 * 查询girls所有列表
	 * @return
	 */
	@GetMapping("/getgirls")
	public List<Girls> getList(){
		
		return girlsInterface.findAll();
	}
	
	/**
	 * 向数据库中添加一个girls数据
	 * @param id girl的id
	 * @param name girl的名字
	 * @return 该girl对象的json格式
	 */
	@PostMapping("/addOneGirls")
	public Girls addOneGirl( @RequestParam(value="age") Integer myage ,@RequestParam(value="name") String name) {
		
		Girls g = new Girls();
		g.setAge(myage);
		g.setName(name);
		return girlsInterface.save(g);
		
	}
	
	/**
	 * post添加girls
	 * @param girl
	 * @param result
	 * @return
	 */
	@PostMapping("/addByEntity")
	public Object addByEntity(@Valid Girls girl,BindingResult result ) {
		
		if(result.hasErrors()) {//注入是发现错误
			return ResultUtil.error(1, result.getFieldError().getDefaultMessage());
		}
		
		return ResultUtil.success(girlsInterface.save(girl));
		
	}
	
	
	/**
	 * 根据id查询女生信息
	 * @param id 女生id
	 * @return 女生实例json格式
	 */
	@GetMapping("/getone/{id}")
	public ResultResp selectOne (@PathVariable("id") Integer id) throws Exception {
		return girlsService.getAge(id);
		
	}
	/**
	 * 根据id更新数据库表字段信息
	 * @param id
	 * @param age
	 * @param name
	 * @return
	 */
	@PutMapping("/update/{id}")
	public String updateOne(@PathVariable("id") Integer id, @RequestParam("age") Integer age,
			@RequestParam("name") String name) {
		
		Girls g = girlsInterface.findOne(id);
		g.setAge(age);
		g.setName(name);
		girlsInterface.save(g);
		return "更新成功";
	}
	
	/**
	 * 根据id来删除相应的字段信息
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	public String deleteOne( @PathVariable("id") Integer id) {
		girlsInterface.delete(id);
		return "删除成功";
	}
	
	/**
	 * 根据年龄来查询女生信息
	 */
	@GetMapping("/getbyage/{age}")
	public List<Girls> findByAge(@PathVariable("age") Integer age){
		return girlsInterface.findByAge(age);
	}
	
	@PostMapping("/insertTwo")
	public String insertTwoG() {
		girlsService.insertTwoGirls();
		return "插入两个成功";
	}

}

