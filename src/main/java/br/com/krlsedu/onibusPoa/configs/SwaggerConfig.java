package br.com.krlsedu.onibusPoa.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"API de linhas de ônibos de Porto Alegre",
				"API de cadastro e consulta das informações de linhas de ônibus de Porto Alegre.",
				"API 1.0.0.0",
				"",
				new Contact("Carlos Eduardo Duarte Schwalm ", "https://github.com/krlsedu", "krlsedu@gmail.com"),
				"",
				"",
				Collections.emptyList()
		);
	}
}
