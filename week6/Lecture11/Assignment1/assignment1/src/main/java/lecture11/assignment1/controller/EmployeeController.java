package lecture11.assignment1.controller;

import lecture11.assignment1.model.Employee;
import lecture11.assignment1.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lecture11.assignment1.model.Salary;
import lecture11.assignment1.model.Title;
import lecture11.assignment1.service.SalaryService;
import lecture11.assignment1.service.TitleService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private TitleService titleService;

    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(employeeService.getAllEmployees(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "*/*")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "*/*")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody Employee employeeDetails) {
        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    employee.setBirthDate(employeeDetails.getBirthDate());
                    employee.setFirstName(employeeDetails.getFirstName());
                    employee.setLastName(employeeDetails.getLastName());
                    employee.setGender(employeeDetails.getGender());
                    employee.setHireDate(employeeDetails.getHireDate());
                    return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/salaries")
    public List<Salary> getSalariesByEmployeeId(@PathVariable int id) {
        return salaryService.getSalariesByEmployeeId(id);
    }

    @PostMapping(value = "/{id}/salaries", consumes = "*/*")
    public Salary addSalary(@PathVariable int id, @RequestBody Salary salary) {
        salary.getId().setEmpNo(id);
        return salaryService.addSalary(salary);
    }

    @PutMapping(value = "/{id}/salaries/{fromDate}", consumes = "*/*")
    public ResponseEntity<Salary> updateSalary(@PathVariable int id, @PathVariable String fromDate, @RequestBody Salary salaryDetails) {
        LocalDate localDate = LocalDate.parse(fromDate);
        Date from = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return ResponseEntity.ok(salaryService.updateSalary(id, from, salaryDetails));
    }

    @GetMapping("/{id}/titles")
    public List<Title> getTitlesByEmployeeId(@PathVariable int id) {
        return titleService.getTitlesByEmployeeId(id);
    }

    @PostMapping(value = "/{id}/titles", consumes = "*/*")
    public Title addTitle(@PathVariable int id, @RequestBody Title title) {
        title.getId().setEmpNo(id);
        return titleService.addTitle(title);
    }

    @PutMapping(value = "/{id}/titles/{fromDate}", consumes = "*/*")
    public ResponseEntity<Title> updateTitle(@PathVariable int id, @PathVariable String fromDate, @RequestBody Title titleDetails) {
        LocalDate localDate = LocalDate.parse(fromDate);
        Date from = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return ResponseEntity.ok(titleService.updateTitle(id, from, titleDetails));
    }
}
