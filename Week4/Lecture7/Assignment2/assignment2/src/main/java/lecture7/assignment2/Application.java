package lecture7.assignment2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import lecture7.assignment2.config.AppConfig;
import lecture7.assignment2.service.EmployeeService;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);

        EmployeeService employeeService = context.getBean(EmployeeService.class);
        employeeService.notifyEmployeeByEmail("johndoe@example.com", "Work Update", "This is a work update message.");

        context.close();
    }
}