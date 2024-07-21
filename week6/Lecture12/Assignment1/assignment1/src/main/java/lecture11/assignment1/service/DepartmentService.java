package lecture11.assignment1.service;

import lecture11.assignment1.model.Department;
import lecture11.assignment1.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(String deptNo) {
        return departmentRepository.findById(deptNo);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(String deptNo, Department departmentDetails) {
        Department department = departmentRepository.findById(deptNo).orElseThrow();
        department.setDeptName(departmentDetails.getDeptName());
        return departmentRepository.save(department);
    }

    public void deleteDepartment(String deptNo) {
        Department department = departmentRepository.findById(deptNo).orElseThrow();
        departmentRepository.delete(department);
    }
}
