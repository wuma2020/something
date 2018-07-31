package com.mangofactory.swagger.springmvc;

import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.Maps;
import com.mangofactory.swagger.ControllerDocumentation;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import com.wordnik.swagger.core.DocumentationOperation;

/**
 * mvc中控制器api reader
 *
 * 		应该是从 spring中读取controller的api信息，放到swagger中
 * 	把controller 和 对应的信息 放到缓存中，然后 当访问的时候，直接取该信息
 *
 */
@Slf4j
public class MvcApiReader {

	//spring mvc 上下文
	private final WebApplicationContext context;
	//自定义的swaggerConfiguration
	private final SwaggerConfiguration config;

	@Getter
	//spring mvc 的url的处理器
	//就是从web上下文中获取的所有的controller的集合
	private Map<String, HandlerMapping> handlerMappingBeans;
	
	@Getter
	//swagger中存放api信息的类  含有
	private Documentation resourceListing;

	//map key 为 controller 的class对象 ， value 为swagger相应的 DocumentationEndPoint对象
	// 													包含 uri 和 @Api 注解中的description
	private final Map<Class<?>,DocumentationEndPoint> resourceListCache = Maps.newHashMap();


	private final Map<Class<?>,ControllerDocumentation> apiCache = Maps.newHashMap();
	
	public MvcApiReader(WebApplicationContext context, SwaggerConfiguration swaggerConfiguration)
	{
		this.context = context;
		config = swaggerConfiguration;
		//handlerMappingBeans就是所有的controller bean的集合
		handlerMappingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.context, HandlerMapping.class, true, false);
		buildMappingDocuments();
	}

	/**
	 * 创建url（handlermapping） 和 Documents 的关系
	 */
	private void buildMappingDocuments() {
		//resourceListing  Documentation类型
		resourceListing = config.newDocumentation();
		
		log.debug("Discovered {} candidates for documentation",handlerMappingBeans.size());
		for (HandlerMapping handlerMapping : handlerMappingBeans.values())
		{
			if (RequestMappingHandlerMapping.class.isAssignableFrom(handlerMapping.getClass()))
			{
				//加工handlerMapping
				processMethod((RequestMappingHandlerMapping) handlerMapping);
			} else {
				log.debug("Not documenting mapping of type {}, as it is not of a recognized type.",handlerMapping.getClass().getName());
			}
		}
	}

	/**
	 *	向 resourceListCache   Map<Class<?>, DocumentationEndPoint> 中添加DocumentationEndPoint信息
	 * @param resource
	 */
	private void addApiListingIfMissing(
			MvcApiResource resource) {
		//查看缓存map中是否有该controller的class对象
		if (resourceListCache.containsKey(resource.getControllerClass()))
			return;
		//如果缓存map没有该controller， 新建一个DocumentationEndPoint 对象
		DocumentationEndPoint endpoint = resource.describeAsEndpoint();
		if (endpoint != null)
		{   //向缓存map中添加 相应的 controller class对象 和 DocumentationEndPoint对象
			resourceListCache.put(resource.getControllerClass(),endpoint);
			log.debug("Added resource listing: {}",resource.toString());
			//向 resourceListing （Documentation）中添加DocumentationEndPoint对象
			resourceListing.addApi(endpoint);
		}
	}

	/**
	 * 加工handlerMapping的方法
	 * @param handlerMapping
	 */
	private void processMethod(RequestMappingHandlerMapping handlerMapping) {
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMapping.getHandlerMethods().entrySet()) {

			HandlerMethod handlerMethod = entry.getValue();
			RequestMappingInfo mappingInfo = entry.getKey();
			
			MvcApiResource resource = new MvcApiResource(handlerMethod,config);
			
			// Don't document our own controllers
			if (resource.isInternalResource())
				continue;

			//向 resourceListing 和 resourceListCache 中添加 该controller 对应的DocumentationEndPoint 对象
			addApiListingIfMissing(resource);

			//向apiCache 中添加相应的controller class 以及 ControllerDocumentation对象
			ControllerDocumentation apiDocumentation = getApiDocumentation(resource);

			for (String requestUri : mappingInfo.getPatternsCondition().getPatterns())
			{
				//向apiDocumentation 的 成员 map中添加 Map<String, com.wordnik.swagger.core.DocumentationEndPoint>信息
				DocumentationEndPoint endPoint = apiDocumentation.getEndPoint(requestUri);
				//向 DocumentationEndPoint 中添加 DocumentationOperation
				appendOperationsToEndpoint(mappingInfo,handlerMethod,endPoint);
				
			}
		}
	}

	/**
	 * 向apiCache Map<Class<?>,ControllerDocumentation> 中加入resource 创建的 ControllerDocumentation
	 * @param resource
	 * @return
	 */
	private ControllerDocumentation getApiDocumentation(MvcApiResource resource) {

		if (!apiCache.containsKey(resource.getControllerClass()))//判断apiCache map 中是否有该 controller的class对象
		{ //如果没有，向apiCache中put该controller class 以及对应的 ControllerDocumentation 对象

			ControllerDocumentation emptyApiDocumentation = resource.createEmptyApiDocumentation();
			if (emptyApiDocumentation != null)
				apiCache.put(resource.getControllerClass(),emptyApiDocumentation);
		}
		//然后返回该 apiCache中的该controller 对应的 ControllerDocumentation 对象
		return apiCache.get(resource.getControllerClass());
	}

	/**
	 *  从 mappingInfo 中获取 RequestMethod ，根据handlerMethod 获取ApiMethodReader，
	 *  然后利用ApiMethodReader 向 DocumentationEndPoint 中添加 DocumentationOperation
	 * @param mappingInfo
	 * @param handlerMethod
	 * @param endPoint
	 */
	private void appendOperationsToEndpoint(
			RequestMappingInfo mappingInfo, HandlerMethod handlerMethod, DocumentationEndPoint endPoint) {
		//根据 handlerMethod 构建 ApiMethodReader 对象  解析 controller 中的 api 的方法
		ApiMethodReader methodDoc = new ApiMethodReader(handlerMethod);
		for (RequestMethod requestMethod : mappingInfo.getMethodsCondition().getMethods())
		{
			//根据 RequestMethod 来获取一个 DocumentationOperation 对象
			DocumentationOperation operation = methodDoc.getOperation(requestMethod);
			//向DocumentationEndPoint 中添加 DocumentationOperation
			endPoint.addOperation(operation);
		}
	}

	/**
	 * 从 缓存apiCache 中 根据参数 apiName获取相应的 ControllerDocmentation
	 * @param apiName
	 * @return
	 */
	public ControllerDocumentation getDocumentation(
			String apiName) {
		
		for (ControllerDocumentation documentation : apiCache.values())
		{
			if (documentation.matchesName(apiName))
				return documentation;
		}
		log.error("Could not find a matching resource for api with name '" + apiName + "'");
		return null;
	}
}