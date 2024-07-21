package lecture11.assignment1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    private String deptNo;

    @Column(name = "dept_name")
    private String deptName;

    @OneToMany(mappedBy = "department")
    private List<DeptEmp> deptEmps;

    @OneToMany(mappedBy = "department")
    private List<DeptManager> deptManagers;
}
