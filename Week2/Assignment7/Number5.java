import java.util.ArrayList;
import java.util.List;

class Page<T> {
    private List<T> items;
    private int pageSize;
    private int pageNumber;

    public Page(List<T> items, int pageSize, int pageNumber) {
        this.items = items;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public List<T> getCurrentPageItems() {
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, items.size());
        return items.subList(fromIndex, toIndex);
    }

    public void nextPage() {
        if ((pageNumber + 1) * pageSize < items.size()) {
            pageNumber++;
        }
    }

    public void previousPage() {
        if (pageNumber > 0) {
            pageNumber--;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    @Override
    public String toString() {
        return "Page " + (pageNumber + 1) + " of " + getTotalPages();
    }
}

class Person {
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", Age: " + age;
    }
}

public class Number5 {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("John", "Doe", 30));
        people.add(new Person("Jane", "Doe", 25));
        people.add(new Person("Alice", "Smith", 35));
        people.add(new Person("Bob", "Brown", 40));
        people.add(new Person("Charlie", "Black", 50));

        Page<Person> personPage = new Page<>(people, 2, 0);

        System.out.println("Initial Page:");
        personPage.getCurrentPageItems().forEach(System.out::println);
        System.out.println(personPage);

        personPage.nextPage();
        System.out.println("\nNext Page:");
        personPage.getCurrentPageItems().forEach(System.out::println);
        System.out.println(personPage);

        personPage.previousPage();
        System.out.println("\nPrevious Page:");
        personPage.getCurrentPageItems().forEach(System.out::println);
        System.out.println(personPage);
    }
}