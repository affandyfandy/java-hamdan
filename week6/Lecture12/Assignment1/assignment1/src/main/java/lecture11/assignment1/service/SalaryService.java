package lecture11.assignment1.service;

import lecture11.assignment1.model.Salary;
import lecture11.assignment1.model.SalaryId;
import lecture11.assignment1.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public List<Salary> getSalariesByEmployeeId(int empNo) {
        return salaryRepository.findByEmployeeEmpNo(empNo);
    }

    public Salary addSalary(Salary salary) {
        return salaryRepository.save(salary);
    }

    public Salary updateSalary(int empNo, Date fromDate, Salary salaryDetails) {
        SalaryId id = new SalaryId();
        id.setEmpNo(empNo);
        id.setFromDate(fromDate);
        Salary salary = salaryRepository.findById(id).orElseThrow();
        salary.setSalary(salaryDetails.getSalary());
        salary.setToDate(salaryDetails.getToDate());
        return salaryRepository.save(salary);
    }
}
