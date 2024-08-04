package lecture11.assignment1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salaries")
public class Salary implements Serializable {

    @EmbeddedId
    private SalaryId id;

    @Column(name = "salary")
    private int salary;

    @Column(name = "to_date")
    private Date toDate;

    @ManyToOne
    @MapsId("empNo")
    @JoinColumn(name = "emp_no")
    @JsonIgnore
    private Employee employee;
}
