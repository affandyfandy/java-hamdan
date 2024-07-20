package lecture7.assignment3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmailService emailService;

    // Constructor Injection
    @Autowired
    public EmployeeService(@Qualifier("singletonEmailService") EmailService emailService) {
        this.emailService = emailService;
    }

    // Field Injection
    @Autowired
    @Qualifier("singletonEmailService")
    private EmailService fieldInjectedEmailService;

    // Setter Injection
    private EmailService setterInjectedEmailService;

    @Autowired
    public void setEmailService(@Qualifier("prototypeEmailService") EmailService emailService) {
        this.setterInjectedEmailService = emailService;
    }

    public void notifyEmployeeByEmail(String email, String subject, String message) {
        // Using Constructor Injection
        System.out.println("Constructor Injection EmailService hashcode: " + emailService.hashCode());
        emailService.sendEmail(email, subject, message);

        // Using Field Injection
        System.out.println("Field Injection EmailService hashcode: " + fieldInjectedEmailService.hashCode());
        fieldInjectedEmailService.sendEmail(email, subject, message);

        // Using Setter Injection
        System.out.println("Setter Injection EmailService hashcode: " + setterInjectedEmailService.hashCode());
        setterInjectedEmailService.sendEmail(email, subject, message);
    }
}
