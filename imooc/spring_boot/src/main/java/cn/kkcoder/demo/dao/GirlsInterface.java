package cn.kkcoder.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.kkcoder.demo.domain.Girls;

public interface GirlsInterface extends JpaRepository<Girls, Integer> {

	/**
	 * 通过年龄来查询女生列表
	 * 注意方法名，必须以 find By  属性名  来命名
	 * @param age
	 * @return
	 */
	public List<Girls> findByAge(Integer age) ;
		
}
