package app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Employee;
import utils.DateUtils;
import utils.FileUtils;

public class AppManager {
    private static AppManager instance = null;
    private List<Employee> employees;

    private AppManager() {
        employees = new ArrayList<>();
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("0 - Break");
            System.out.println("1 - Select file, import data");
            System.out.println("2 - Add new Employee");
            System.out.println("3 - Filter by name (like), id (like), date of birth - year (equal), department (equal)");
            System.out.println("4 - Filter and export to csv file, order by date of birth");
            System.out.println("5 - Print all employees");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    importData(scanner);
                    break;
                case 2:
                    addNewEmployee(scanner);
                    break;
                case 3:
                    filterEmployees(scanner);
                    break;
                case 4:
                    exportFilteredEmployees(scanner);
                    break;
                case 5:
                    printAllEmployees();
                    break;
                case 0:
                    System.out.println("Have a nice day!!!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void importData(Scanner scanner) {
        System.out.print("Importing the Data... ");
        String xlsxFilePath = "src/main/java/file/ImportData.csv";
        String csvFilePath = "src/main/java/file/ExportData.csv";
        try {
            FileUtils.convertXlsxToCsv(xlsxFilePath, csvFilePath);
            System.out.println("File converted successfully. Data in the CSV file:");

            List<String[]> csvData = FileUtils.readCsv(csvFilePath);
            int i = 0;

            for (String[] row : csvData) {
                System.out.println(String.join(", ", row));
                if (i == 0) {
                    i++;
                    continue;
                }
                String id = row[0];
                String name = row[1];
                LocalDate dateOfBirth = DateUtils.parseDate(row[2]);
                String address = row[3];
                String department = row[4];

                Employee employee = new Employee(id, name, dateOfBirth, address, department);
                employees.add(employee);
            }
        } catch (IOException e) {
            System.out.println("Error converting file: " + e.getMessage());
        }
    }

    private void addNewEmployee(Scanner scanner) {
        System.out.print("Enter employee ID: ");
        scanner.nextLine();
        String id = scanner.nextLine();
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter date of birth (dd/mm/yyyy): ");
        String dobStr = scanner.nextLine();
        LocalDate dateOfBirth = DateUtils.parseDate(dobStr);
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter department: ");
        String department = scanner.nextLine();

        Employee employee = new Employee(id, name, dateOfBirth, address, department);
        employees.add(employee);
        System.out.println("Employee added successfully.");
    }

    private void filterEmployees(Scanner scanner) {
        System.out.println("1 - Filter by name");
        System.out.println("2 - Filter by birth year");
        System.out.println("3 - Filter by ID");
        System.out.println("4 - Filter by departments");
        System.out.print("Enter your choice: ");
        List<Employee> filteredEmployees = new ArrayList<>();
        int newChoice = scanner.nextInt();
        Scanner scanner3 = new Scanner(System.in);
        switch (newChoice) {
            case 1:
                System.out.print("Enter Name: ");
                String filterCriteria = scanner3.nextLine();
                for (Employee emp : employees) {
                    boolean matches = emp.getName().contains(filterCriteria);
                    if (matches) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
            case 2:
                System.out.print("Enter birth year: ");
                String filterCriteria2 = scanner3.nextLine();
                for (Employee emp : employees) {
                    int year = Integer.parseInt(filterCriteria2);
                    boolean matches = emp.getDateOfBirth().getYear() == year;
                    if (matches) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
            case 3:
                System.out.print("Enter ID: ");
                String filterCriteria3 = scanner3.nextLine();
                for (Employee emp : employees) {
                    boolean matches = emp.getId().contains(filterCriteria3);
                    if (matches) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
            case 4:
                System.out.print("Enter Departments: ");
                String filterCriteria4 = scanner3.nextLine();
                for (Employee emp : employees) {
                    boolean matches = emp.getDepartment().contains(filterCriteria4);
                    if (matches) {
                        filteredEmployees.add(emp);
                    }
                }
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }

        System.out.println("Filtered Employees:");
        for (Employee emp : filteredEmployees) {
            System.out.println(emp);
        }
    }

    private void exportFilteredEmployees(Scanner scanner) {
        System.out.print("Exporting the csv file: ");
        String csvFilePath = "src/main/java/file/ExportData.csv";

        employees.sort((e1, e2) -> e1.getDateOfBirth().compareTo(e2.getDateOfBirth()));

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "Name", "Date of Birth", "Address", "Department"});
        for (Employee emp : employees) {
            data.add(new String[]{emp.getId(), emp.getName(), DateUtils.formatDate(emp.getDateOfBirth()), emp.getAddress(), emp.getDepartment()});
        }

        try {
            FileUtils.writeCsv(csvFilePath, data);
            System.out.println("Data exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    public void printAllEmployees() {
        System.out.println("All Employees Datas:");
        for (Employee emp : employees) {
            System.out.println(emp);
        }
    }
}

