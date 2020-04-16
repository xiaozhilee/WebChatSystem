package com.chat.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class resource extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/image/**").addResourceLocations("file:/usr/WebChatSystem/user_img/");
//        registry.addResourceHandler("/file/image/**").addResourceLocations("file:D://美图/");
        super.addResourceHandlers(registry);
    }
}
