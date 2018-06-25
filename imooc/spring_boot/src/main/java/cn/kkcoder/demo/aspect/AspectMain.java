package cn.kkcoder.demo.aspect;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



/**
 * 切面类
 * @author : static-mkk
 * @time   : 24 Jun 2018 <br/>
 *
 */
//Aspect注解说明该类是个切面类
//@Component注解 是为了在容器启动时把该类添加到容器中
@Aspect
@Component
public class AspectMain {
	
	//需要一个log对象来打印日志
	private final static Logger logger = LoggerFactory.getLogger(AspectMain.class);
	
	
	/**
	 * 设置一个切点，监听具体的那些方法
	 */
	@Pointcut(value="execution(public * cn.kkcoder.demo.controller..*(..))")
	public void point() {
		
	}
	
	/**
	 * 在切点方法执行之前执行，，这样就省去了重复的写 execution的工作了
	 * JionPoint 类是通过反射来获取切点的类的信息
	 */
	@Before("point()")
	public void before(JoinPoint jionpoint) {
		/*
		 * 这里记录一下http请求，所以会使用一些内部的类  
		 * 这些方法都是上下问提供的静态的方法贡使用的
		 */
		ServletRequestAttributes  attibute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request =  attibute.getRequest();
		//url
		logger.info("url={}",request.getRequestURL().toString());
		//method
		logger.info("method={}",request.getMethod());
		//id
		logger.info("id={}",request.getRemoteHost());
		//类方法   调用的类的方法
		logger.info("class={}" ,"类名： "+ jionpoint.getSignature().getDeclaringTypeName()+ "."+ jionpoint.getSignature().getName());
		//类的参数
		logger.info("args={}",jionpoint.getArgs());
	}
	/**
	 * 在切点方法执行之后执行，，这样就省去了重复的写 execution的工作了
	 */
	@After("point()")
	public void doAfter() {
		System.out.println("我是after");
	}
	
	/**
	 * 获取返回response对象
	 * @param obj  obj要与AfterReturning的returning的值相同，即该注解会把response的对象赋值类名为obj的形参。所以方法中的形参名为obj
	 * 				
	 */
	@AfterReturning(returning="obj", pointcut="point()" )
	public void afterReturning(Object obj) {
		logger.info(obj.toString());
	}

}
