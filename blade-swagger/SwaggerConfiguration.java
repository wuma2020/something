package com.mangofactory.swagger;

import lombok.Data;

import com.mangofactory.swagger.springmvc.MvcApiResource;
import com.wordnik.swagger.core.Documentation;

@Data
public class SwaggerConfiguration {

	/**
	 * 实例化一个SwaggerConfigiration 对象
	 * @param apiVersion
	 * @param swaggerVersion
	 * @param basePath
	 */
	public SwaggerConfiguration(String apiVersion, String swaggerVersion,
			String basePath) {
		this.apiVersion = apiVersion;
		this.swaggerVersion = swaggerVersion;
		this.basePath = basePath;
	}
	private String swaggerVersion;
	private String apiVersion;
	private String basePath;

	/**
	 * 新建一个ControllerDocumentation类对象
	 * @param resource
	 * @return
	 */
	public ControllerDocumentation newDocumentation(MvcApiResource resource) {
		return new ControllerDocumentation(apiVersion, swaggerVersion, basePath, resource);
	}

	/**
	 * 新建一个Documentation实例对象
	 * @return
	 */
	public Documentation newDocumentation()
	{
		return new Documentation(apiVersion, swaggerVersion, basePath, null);
	}
}
