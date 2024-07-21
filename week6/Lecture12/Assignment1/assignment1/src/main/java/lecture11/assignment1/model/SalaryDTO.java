package lecture11.assignment1.model;

import lecture11.assignment1.model.Salary;
import lombok.Data;

import java.util.Date;

@Data
public class SalaryDTO {
    private int salary;
    private Date fromDate;
    private Date toDate;

    public SalaryDTO(Salary salary) {
        this.salary = salary.getSalary();
        this.fromDate = salary.getId().getFromDate();
        this.toDate = salary.getToDate();
    }
}
