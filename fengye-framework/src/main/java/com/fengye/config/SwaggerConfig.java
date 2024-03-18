package com.fengye.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  
  @Bean
  public Docket defaultApi2() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            // 这里一定要标注你控制器的位置
            .apis(RequestHandlerSelectors.basePackage("com.fengye.controller"))
            .paths(PathSelectors.any())
            .build();
  }

  /**
   * api 信息
   * @return 信息描述
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("我的博客-接口文档")
            .description("找学习资料，上博客!")
            .termsOfServiceUrl("https://github.com/")
            .contact(new Contact("fengye","https://fengye.fan/","fengye@qq.com"))
            .version("1.0")
            .build();
  }
}
