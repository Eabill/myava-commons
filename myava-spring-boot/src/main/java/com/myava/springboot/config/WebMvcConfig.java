package com.myava.springboot.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.myava.springboot.interceptor.LoginInterceptor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * WEB配置
 *
 * @author biao
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		// 处理日期格式
		fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");
		// 处理中文乱码
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters((HttpMessageConverter<?>) fastJsonHttpMessageConverter);
	}

	/**
	 * 启用Hibernate-validator校验框架
	 * @return
	 */
//	@Bean
//	public MethodValidationPostProcessor methodValidationPostProcessor() {
//		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
//		methodValidationPostProcessor.setValidator(validator());
//		return methodValidationPostProcessor;
//	}
	
	/**
	 * failFast：true 快速失败返回模式 false 普通模式
	 * @return
	 */
//	@Bean
//	public Validator validator() {
//		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
//				.configure()
//				.failFast(false)
//				.buildValidatorFactory();
//		Validator validator = validatorFactory.getValidator();
//		return validator;
//	}
	
	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/**")
			.excludePathPatterns("/", "/static/**")
			.excludePathPatterns("/swagger-resources/**", "/v2/api-docs");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		// 解决SWAGGER404报错
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index.html");
	}
	
}
