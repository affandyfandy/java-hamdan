package lecture7.assignment1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lecture7.assignment1.controller.EmployeeWork;
import lecture7.assignment1.model.Employee;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeWork employeeWork() {
        return new EmployeeWork();
    }

    @Bean
    public Employee employee() {
        return new Employee(1, "GL", 30, employeeWork());
    }
}
