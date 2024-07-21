package lecture11.assignment1.repository;

import lecture11.assignment1.model.Salary;
import lecture11.assignment1.model.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
    List<Salary> findByEmployeeEmpNo(int empNo);
}
