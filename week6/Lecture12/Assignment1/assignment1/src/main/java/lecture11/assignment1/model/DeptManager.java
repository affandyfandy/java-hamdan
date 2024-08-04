package lecture11.assignment1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dept_manager")
public class DeptManager {

    @Embeddable
    @Data
    public static class DeptManagerId implements Serializable {
        private Integer empNo;
        private String deptNo;
    }

    @EmbeddedId
    private DeptManagerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("empNo")
    @JoinColumn(name = "emp_no")
    @JsonIgnore
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deptNo")
    @JoinColumn(name = "dept_no")
    @JsonIgnore
    private Department department;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;
}
