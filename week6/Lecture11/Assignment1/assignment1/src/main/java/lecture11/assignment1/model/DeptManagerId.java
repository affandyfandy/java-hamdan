package lecture11.assignment1.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class DeptManagerId implements Serializable {
    private Integer empNo;
    private String deptNo;
}
