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

public class Teacher {
    private String name;
    private int age;
    private Subject subject;

    public Teacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Teacher(String name, int age, Subject subject) {
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
        System.out.println("Teacher "+name+" is teaching "+subject.getName()+" for " + subject.getClassid());
    }

    public static void main(String[] args) {
        Subject math = new Subject("Mathematics");
        math.setClassid("Class 1");
        Teacher teacher = new Teacher("Tam", 32,math);
        teacher.teach();
    }
    
}
