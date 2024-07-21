package lecture11.assignment1.repository;

import lecture11.assignment1.model.DeptManager;
import lecture11.assignment1.model.DeptManagerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptManagerRepository extends JpaRepository<DeptManager, DeptManagerId> {
}
