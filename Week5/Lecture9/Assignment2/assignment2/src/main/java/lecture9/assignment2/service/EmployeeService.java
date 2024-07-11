package lecture9.assignment2.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lecture9.assignment2.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int theId);

    void save(Employee theEmployee);

    void deleteById(int theId);

    Page<Employee> findPaginated(Pageable pageable);
}
