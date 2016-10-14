package com.sol.wwbs.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@PropertySource("classpath:config.properties")
@Configuration
@EnableWebMvc
@ComponentScan("com.mom.wwbs")
public class WebServletConfig extends WebMvcConfigurerAdapter {

    @Resource
    private Environment env;
    
	@Bean
	public ViewResolver ViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix(env.getProperty("mvc.perfix"));
		resolver.setSuffix(env.getProperty("mvc.suffix"));
		resolver.setExposeContextBeansAsAttributes(true);
		
		System.out.println("ViewResolver");
		return resolver;
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		System.out.println("configureDefaultServletHandling");
		configurer.enable();
	}

}
