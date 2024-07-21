package lecture11.assignment1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TitleId implements Serializable {

    @Column(name = "emp_no")
    private int empNo;

    @Column(name = "title")
    private String title;

    @Column(name = "from_date")
    private Date fromDate;
}
