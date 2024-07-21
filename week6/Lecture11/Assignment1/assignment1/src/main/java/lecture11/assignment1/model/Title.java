package lecture11.assignment1.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "titles")
public class Title {

    @Embeddable
    @Data
    public static class TitleId implements Serializable {
        private Integer empNo;
        @Column(name = "from_date")
        private Date fromDate;
    }

    @EmbeddedId
    private TitleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("empNo")
    @JoinColumn(name = "emp_no")
    @JsonIgnore
    private Employee employee;

    private String title;

    @Column(name = "from_date", insertable = false, updatable = false)
    private Date fromDate;

    private Date toDate;
}
