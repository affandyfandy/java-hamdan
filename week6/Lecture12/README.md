
# Assignment 1: Spring Data JPA #2

In this project we will update our previous project to support Search employee info with dynamic criteria.

## Updated Features

### 1. Search Employee Info with Dynamic Criteria

This feature allows you to search for employees based on various criteria dynamically. You can pass different parameters to filter the employee data according to your needs.

### 2. Implementation of DTO (Data Transfer Object)

DTOs are used to transfer data between the application layers. They help in encapsulating the data and improving the performance by reducing the number of remote calls.

## Changes to the Project

### 1. Adding Dynamic Criteria Search

To implement dynamic criteria search, the following updates were made:

- Created `EmployeeSpecification` class to handle dynamic criteria.
- Updated `EmployeeRepository` to extend `JpaSpecificationExecutor`.
- Added a new method in `EmployeeService` to search employees based on criteria.
- Updated `EmployeeController` to include the new search endpoint.

### 2. Updating the `EmployeeController`

The `EmployeeController` was updated to include the search functionality and the DTO pattern. The following changes were made:

- Added a new endpoint for searching employees with dynamic criteria.
- Updated the existing endpoints to use DTOs where necessary.

## How to Run the Project

1. **Set Up the Database**

    We will use the previous database for this updated project.
```sql
CREATE TABLE employees (
    emp_no INT AUTO_INCREMENT,
    birth_date DATE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender ENUM('M', 'F'),
    hire_date DATE,
    PRIMARY KEY (emp_no)
);

CREATE TABLE departments (
    dept_no CHAR(4),
    dept_name VARCHAR(40),
    PRIMARY KEY (dept_no)
);

CREATE TABLE dept_emp (
    emp_no INT,
    dept_no CHAR(4),
    from_date DATE,
    to_date DATE,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no),
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no)
);

CREATE TABLE dept_manager (
    emp_no INT,
    dept_no CHAR(4),
    from_date DATE,
    to_date DATE,
    PRIMARY KEY (emp_no, dept_no),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no),
    FOREIGN KEY (dept_no) REFERENCES departments(dept_no)
);

CREATE TABLE salaries (
    emp_no INT,
    from_date DATE,
    to_date DATE,
    salary INT,
    PRIMARY KEY (emp_no, from_date),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no)
);

CREATE TABLE titles (
    emp_no INT,
    title VARCHAR(50),
    from_date DATE,
    to_date DATE,
    PRIMARY KEY (emp_no, from_date, title),
    FOREIGN KEY (emp_no) REFERENCES employees(emp_no)
);

-- Insert mock data
INSERT INTO employees (birth_date, first_name, last_name, gender, hire_date) VALUES
('1979-01-01', 'John', 'Doe', 'M', '2019-01-01'),
('1985-05-15', 'Jane', 'Smith', 'F', '2020-06-15');

INSERT INTO departments (dept_no, dept_name) VALUES
('d001', 'Engineering'),
('d002', 'HR'),
('d003', 'Finance');

INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES
(1, 'd001', '2019-01-01', '2022-01-01'),
(2, 'd002', '2020-06-15', '2023-06-15');

INSERT INTO dept_manager (emp_no, dept_no, from_date, to_date) VALUES
(1, 'd001', '2019-01-01', '2022-01-01'),
(2, 'd002', '2020-06-15', '2023-06-15');

INSERT INTO salaries (emp_no, from_date, to_date, salary) VALUES
(1, '2019-01-01', '2022-01-01', 50000),
(2, '2020-06-15', '2023-06-15', 60000);

INSERT INTO titles (emp_no, title, from_date, to_date) VALUES
(1, 'Engineer', '2019-01-01', '2022-01-01'),
(2, 'Manager', '2020-06-15', '2023-06-15');
```

2. **Build and Run the Application**

   ```sh
   mvn clean install
   mvn spring-boot:run
   ```

3. **Test the Endpoints**

   You can use the provided Postman collection.