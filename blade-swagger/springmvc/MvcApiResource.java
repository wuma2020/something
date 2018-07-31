package com.mangofactory.swagger.springmvc;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import com.mangofactory.swagger.ControllerDocumentation;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.springmvc.controller.DocumentationController;
import com.wordnik.swagger.core.Api;
import com.wordnik.swagger.core.DocumentationEndPoint;

/**
 * 根据构造参数传进来的 HandlerMethod 和 SwaggerConfiguration
 * 		创建两个方法：分别返回该handlerMethod 对应的 DocumentationEndPoint 对象
 * 											和  ControllerDocumentation
 * 解析 controller 对象实例 的 类
 * 把给的api class对象 生成一个资源对象
 * 成员： HandlerMethod
 * 			Class<?> controllerClass;
 * 		SwaggerConfiguration configuration;
 * @author martypitt
 *
 */
@Slf4j
public class MvcApiResource {

	@Getter
	private final HandlerMethod handlerMethod;
	private final Class<?> controllerClass;
	private final SwaggerConfiguration configuration;

	public MvcApiResource(HandlerMethod handlerMethod, SwaggerConfiguration configuration) {
		this.handlerMethod = handlerMethod;
		this.configuration = configuration;
		this.controllerClass = handlerMethod.getBeanType();
	}

	/**
	 * 解析controller的 @RequestMapping 的 uri 和 @Api 的 description
	 * 		转换成 swagger 的 DocumentationEndPoint 对象
	 * @return 该对象
	 */
	public DocumentationEndPoint describeAsEndpoint()
	{
		//传进去 注解@RequestMapping 的 uri（String） 和 注解@api 的 description (String )
		DocumentationEndPoint endPoint = new DocumentationEndPoint(getControllerUri(),getApiDescription());
		return endPoint;
	}

	/**
	 * 根据该controller，新建一个 ControllerDocumentation 对象
	 * @return
	 */
	public ControllerDocumentation createEmptyApiDocumentation()
	{
		String resourcePath = getControllerUri();
		if (resourcePath == null)
			return null;
		
		return configuration.newDocumentation(this);
	}

	/**
	 * 获取该controller上的注解@api的description信息
	 * @return
	 */
	private String getApiDescription()
	{
		Api apiAnnotation = controllerClass.getAnnotation(Api.class);
		if (apiAnnotation == null)
			return null;
		return apiAnnotation.description();
		
	}

	/**
	 * 获取该 controllerClass 上的 注解@requestMapping 里面的uri
	 * @return
	 */
	public String getControllerUri() {
		RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
		if (requestMapping == null)
		{
			log.warn("Class {} has handler methods, but no class-level @RequestMapping.  No documentation will be generated",controllerClass.getName());
			return null;
		}
		String[] requestUris = requestMapping.value();
		if (requestUris == null || requestUris.length == 0)
		{
			log.warn("Class {} contains a @RequestMapping, but could not resolve the uri.  No documentation will be generated", controllerClass.getName());
			return null;
		}
		if (requestUris.length > 1)
		{
			log.warn("Class {} contains a @RequestMapping with multiple uri's.  Only the first one will be documented.");
		}
		String requestUri = requestUris[0];
		return requestUri;
	}
	
	@Override
	public String toString()
	{
		return "ApiResource for " + controllerClass.getSimpleName() + " at " + getControllerUri();
	}

	/**
	 * 返回 该 HandlerMethod 对应的 控制器(controller)的class对象
	 * @return
	 */
	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public boolean isInternalResource() {
		return controllerClass == DocumentationController.class;
	}

	
}
