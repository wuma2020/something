package cn.kkcoder.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * 实体类 对应数据库一张表
 * @author : static-mkk
 * @time   : 24 Jun 2018 <br/>
 *
 */
@Entity
public class Girls {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	/*
	 * 对age加上@min标签，在注入实体girls时，进行验证.
	 */
	@Min(value=18,message="年龄小于18，false")
	private Integer age;
	private String name;
	
	
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Girls() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Girls [id=" + id + ", age=" + age + ", name=" + name + "]";
	}
	
}
