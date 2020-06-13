package com.myava.springboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger文档配置类
 *
 * @author biao
 */
@Data
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = SwaggerConfig.PREFIX)
public class SwaggerConfig {
	
	public static final String PREFIX = "swagger";
	
	/**
	 * API接口包路径
	 */
	private String basePackage;
	
	/**
	 * API标题
	 */
	private String title;
	
	/**
	 * API描述
	 */
	private String description;
	
	/**
	 * 版本号
	 */
	private String version;
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }

}
