package lecture7.assignment2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private EmailService emailService;  

    // Constructor Injection
    @Autowired
    public EmployeeService(EmailService emailService) {
        this.emailService = emailService;
    }

    // Field Injection
    @Autowired
    private EmailService fieldInjectedEmailService;

    // Setter Injection
    private EmailService setterInjectedEmailService;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.setterInjectedEmailService = emailService;
    }

    public void notifyEmployeeByEmail(String email, String subject, String message) {
        // Using Constructor Injection
        emailService.sendEmail(email, subject, message);

        // Using Field Injection
        fieldInjectedEmailService.sendEmail(email, subject, message);

        // Using Setter Injection
        setterInjectedEmailService.sendEmail(email, subject, message);
    }
}