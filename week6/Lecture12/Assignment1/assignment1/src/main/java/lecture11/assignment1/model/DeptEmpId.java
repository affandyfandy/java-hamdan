package lecture11.assignment1.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class DeptEmpId implements Serializable {
    private Integer empNo;
    private String deptNo;
}
