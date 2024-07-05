package lecture7.assignment3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import lecture7.assignment3.service.EmailService;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RequestScopeController {

    @Autowired
    @Qualifier("singletonEmailService")
    private EmailService emailService;

    @GetMapping("/v1/testRequestScope")
    public String testRequestScope() {
        // Print hashcode to verify request scope
        System.out.println("RequestScopeController hashcode: " + this.hashCode());
        System.out.println("EmailService hashcode: " + emailService.hashCode());
        return "Check the console for hashcode outputs.";
    }
}