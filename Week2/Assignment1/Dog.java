class Dog {
    // States
    private String color;
    private String name;
    private String breed;

    // Constructor
    public Dog(String color, String name, String breed) {
        this.color = color;
        this.name = name;
        this.breed = breed;
    }

    // Behaviors
    public void wagTail() {
        System.out.println(name + " is wagging its tail.");
    }

    public void bark() {
        System.out.println(name + " is barking.");
    }

    public void eat() {
        System.out.println(name + " is eating.");
    }

    // Getters and Setters
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    // Main method for testing
    public static void main(String[] args) {
        Dog myDog = new Dog("Brown", "Buddy", "Labrador");
        myDog.wagTail();
        myDog.bark();
        myDog.eat();
    }
}