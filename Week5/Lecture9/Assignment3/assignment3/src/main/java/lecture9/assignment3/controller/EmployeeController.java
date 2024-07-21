package lecture9.assignment3.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lecture9.assignment3.model.Employee;
import lecture9.assignment3.service.EmployeeService;
import lecture9.assignment3.util.PDFGenerator;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public String viewHomePage(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage = employeeService.findPaginated(pageable);
        model.addAttribute("employeePage", employeePage);
        return "employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showNewEmployeeForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.save(employee);
        return "redirect:/employees/list";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "employees/employee-form";
    }

    @PostMapping("/delete")
    public String deleteEmployee(@RequestParam("employeeId") int id) {
        employeeService.deleteById(id);
        return "redirect:/employees/list";
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "employees/upload-form";
    }

    @PostMapping("/upload")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            return "employees/upload-form";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser parser = CSVFormat.DEFAULT.withHeader("ID", "Name", "DateOfBirth", "Address", "Department", "Salary").withSkipHeaderRecord(true).parse(reader);
            for (CSVRecord record : parser) {
                if (record.isConsistent() && record.size() == 6) {
                    try {
                        Employee employee = new Employee();
                        employee.setId(Long.parseLong(record.get("ID").replace("ABC_", "")));
                        employee.setName(record.get("Name"));
                        employee.setDob(new SimpleDateFormat("dd/MM/yyyy").parse(record.get("DateOfBirth")));
                        employee.setAddress(record.get("Address"));
                        employee.setDepartment(record.get("Department"));
                        employee.setSalary(Integer.parseInt(record.get("Salary")));
                        employeeService.save(employee);
                    } catch (ParseException e) {
                        logger.error("Error parsing date for record: {}", record, e);
                        model.addAttribute("message", "Error parsing date for record: " + record);
                        return "employees/upload-form";
                    } catch (NumberFormatException e) {
                        logger.error("Error parsing number for record: {}", record, e);
                        model.addAttribute("message", "Error parsing number for record: " + record);
                        return "employees/upload-form";
                    }
                } else {
                    logger.error("Inconsistent record: {}", record);
                    model.addAttribute("message", "Inconsistent record: " + record);
                    return "employees/upload-form";
                }
            }
            model.addAttribute("message", "File uploaded successfully.");
        } catch (IOException e) {
            logger.error("Error reading file", e);
            model.addAttribute("message", "An error occurred while processing the file.");
        }

        model.addAttribute("message", "File uploaded successfully.");
        return "redirect:/employees/list?success";
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<InputStreamResource> generatePDF() throws IOException {
        List<Employee> employees = employeeService.findAll();
        ByteArrayInputStream bis = PDFGenerator.generateEmployeePDF(employees);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=employees.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
