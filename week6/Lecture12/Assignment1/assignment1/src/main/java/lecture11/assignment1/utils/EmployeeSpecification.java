package lecture11.assignment1.utils;

import lecture11.assignment1.model.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Date;
import java.util.Map;

public class EmployeeSpecification {

    public static Specification<Employee> getSpecifications(Map<String, String> criteria) {
        Specification<Employee> spec = Specification.where(null);

        if (criteria.containsKey("firstName")) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("firstName"), "%" + criteria.get("firstName") + "%"));
        }

        if (criteria.containsKey("lastName")) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("lastName"), "%" + criteria.get("lastName") + "%"));
        }

        if (criteria.containsKey("gender")) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("gender"), criteria.get("gender")));
        }

        if (criteria.containsKey("hireDate")) {
            Date hireDate = Date.valueOf(criteria.get("hireDate"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("hireDate"), new java.util.Date(hireDate.getTime())));
        }

        return spec;
    }
}
