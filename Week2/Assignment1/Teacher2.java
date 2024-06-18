

class Subject{
    private String name;
    private String classid;

    public Subject(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

}

class Student{
    private String name;
    private int age;
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    public void learn(Subject[] subject){
        System.out.print("The student will learning some "+subject[0].getName());
        for(int i = 1;i<subject.length;i++){
            System.out.print(", "+subject[i].getName());
        }
    }
}

public class Teacher2 {
    private String name;
    private int age;
    private Subject subject;

    public Teacher2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Teacher2(String name, int age, Subject subject) {
        this.name = name;
        this.age = age;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void teach(){
        System.out.println("The teacher will teaching a " + subject.getName());
    }

    public static void main(String[] args) {
        Subject math = new Subject("Mathematics");
        math.setClassid("Class 1");
        Subject english = new Subject("English");
        english.setClassid("Class 2");
        Subject java = new Subject("Java");
        java.setClassid("Class 3");
        Subject[] subjects = {math,english,java};
        Teacher2 teacher = new Teacher2("Tam", 32,math);
        teacher.teach();
        Student student = new Student("Mat", 12);
        student.learn(subjects);
    }
    
}
