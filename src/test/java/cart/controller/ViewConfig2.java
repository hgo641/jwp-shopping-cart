package cart.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@TestConfiguration
public class ViewConfig2 implements WebMvcConfigurer {
    @Bean
    public ViewResolver viewResolver() {
        return new ThymeleafViewResolver();
    }
}