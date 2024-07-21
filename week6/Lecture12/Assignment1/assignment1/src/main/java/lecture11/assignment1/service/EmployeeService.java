package lecture11.assignment1.service;

import lecture11.assignment1.model.Employee;
import lecture11.assignment1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lecture11.assignment1.utils.EmployeeSpecification;
import org.springframework.data.jpa.domain.Specification;
import java.util.Optional;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Employee> getAllEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

    public Optional<Employee> getEmployeeById(int empNo) {
        return employeeRepository.findById(empNo);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(int empNo) {
        employeeRepository.deleteById(empNo);
    }

    public List<Employee> searchEmployees(Map<String, String> criteria) {
        Specification<Employee> spec = EmployeeSpecification.getSpecifications(criteria);
        return employeeRepository.findAll(spec);
    }
}
