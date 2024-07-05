package lecture7.assignment3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import lecture7.assignment3.service.EmailServiceImpl;

@Configuration
@ComponentScan(basePackages = "lecture7.assignment3")
public class AppConfig {

    @Bean
    @Scope("singleton")
    public EmailServiceImpl singletonEmailService() {
        return new EmailServiceImpl();
    }

    @Bean
    @Scope("prototype")
    public EmailServiceImpl prototypeEmailService() {
        return new EmailServiceImpl();
    }
}