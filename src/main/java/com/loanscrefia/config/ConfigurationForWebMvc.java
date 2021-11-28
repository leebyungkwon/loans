package com.loanscrefia.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class ConfigurationForWebMvc implements WebMvcConfigurer {
	
	@Autowired
	ConfigurationForInterceptor certificationInterceptor;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		String redirectUrl = "redirect:/front/index";
		try {
			SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
			Date currentDt = new Date();
			
			Date d1 = dateFormatParser.parse(dateFormatParser.format(currentDt));
			Date d2 = dateFormatParser.parse("2021-11-29 10:00:00");

			if(d1.compareTo(d2) >= 0) {
				redirectUrl = "redirect:/login";
			} 
			
		}catch(ParseException e) {
			
		}finally {
			registry.addViewController("/").setViewName(redirectUrl);
		}
	}
	
	@Override
	public void addInterceptors (InterceptorRegistry registry) {
		registry.addInterceptor(certificationInterceptor)
			.excludePathPatterns("/favicon.ico","/error")
			.excludePathPatterns("/assets/**")
			.excludePathPatterns("/static/**");
	}
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(20);
    }
	
	@Bean
    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new XssEscapeServletFilter());
        filterRegistration.setOrder(1);
        filterRegistration.addUrlPatterns("/*");

        return filterRegistration;
    }
}
