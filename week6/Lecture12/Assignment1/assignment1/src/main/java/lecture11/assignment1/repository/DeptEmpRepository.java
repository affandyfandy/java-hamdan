package lecture11.assignment1.repository;

import lecture11.assignment1.model.DeptEmp;
import lecture11.assignment1.model.DeptEmpId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {
}
