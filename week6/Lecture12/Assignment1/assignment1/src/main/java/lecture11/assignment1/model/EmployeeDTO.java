package lecture11.assignment1.model;

import lecture11.assignment1.model.Employee;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeeDTO {
    private int empNo;
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;

    private List<SalaryDTO> salaries;
    private List<TitleDTO> titles;

    public EmployeeDTO(Employee employee) {
        this.empNo = employee.getEmpNo();
        this.birthDate = employee.getBirthDate();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.gender = employee.getGender();
        this.hireDate = employee.getHireDate();
        this.salaries = employee.getSalaries().stream().map(SalaryDTO::new).collect(Collectors.toList());
        this.titles = employee.getTitles().stream().map(TitleDTO::new).collect(Collectors.toList());
    }
}
