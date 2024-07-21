package lecture11.assignment1.repository;

import lecture11.assignment1.model.Title;
import lecture11.assignment1.model.Title.TitleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleRepository extends JpaRepository<Title, TitleId> {
    List<Title> findByEmployeeEmpNo(int empNo);
}
