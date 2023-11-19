package com.shop;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configurable
public class WebConfig implements WebMvcConfigurer {

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//
//		registry.addViewController("/").setViewName("home");
//	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		registry.addResourceHandler("/media/**").addResourceLocations("file:///C:/Users/naiks/eclipse-SpringBootProjects/ShoppingCartProject/src/main/resources/static/media/");
	}
}
