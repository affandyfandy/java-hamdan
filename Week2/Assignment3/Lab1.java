import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException; // Import the Scanner class to read text files
import java.util.Scanner;   // Import the FileWriter class

public class Lab1 {
  public static void main(String[] args) {
    try {
      File myObj = new File("test1.txt");
      Scanner myReader = new Scanner(myObj);
      try {
        FileWriter myWriter = new FileWriter("test2.txt");
        while (myReader.hasNextLine()) {
          String data = myReader.nextLine();
          System.out.println(data);
          myWriter.write(data);
        }
        myWriter.close();  
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      System.out.println("Successfully wrote to the file.");
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}