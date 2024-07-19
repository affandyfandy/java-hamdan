package lecture11.assignment1.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
public class SalaryId implements Serializable {
    private Integer empNo;

    @Column(name = "from_date")
    private Date fromDate;
}