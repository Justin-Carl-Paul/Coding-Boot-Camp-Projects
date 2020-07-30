import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class Book {

    //Class attributes
    String id;
    String title;
    String author;
    int quantity;

    //Class Constructor for Scanner object
    public Book(Scanner scanner){

        //While loop which ensures that the book ID entered is of the correct length
        while(true){

            System.out.println("Enter the book ID: ");
            this.id = scanner.nextLine();

            if(this.id.length()>4){

                System.out.println("ERROR - Book id may not be longer than 4 digits. 0-9999");
                continue;
            }
            else{

                break;
            }
        }


        System.out.println("Enter the book title: ");
        this.title = scanner.nextLine();

        System.out.println("Enter the book author: ");
        this.author = scanner.nextLine();

        
        //While loop that ensures that the quantity entered by the user can be parsed to an int
        while(true){

            try{

                System.out.println("Enter the book quantity: ");
                this.quantity = Integer.parseInt(scanner.nextLine());
                break;
            }
            catch(NumberFormatException e){
    
                System.out.println("ERROR - The quantity was enetered incorrectly. Quantity must be of type integer.");
            }
        }
    }

    //Class Constructor for ResultSet object
    public Book(ResultSet results){

        try{

            this.id = results.getString("id");
            this.title = results.getString("title");
            this.author = results.getString("author");
            this.quantity = results.getInt("quantity");
        }
        catch(SQLException e){

            e.printStackTrace();
        }

    }

    //Book to write string method returns a string of the book which can be passed to a SQL statement
    public String toWriteString(){

        String writeString = "INSERT INTO book VALUES(";
        writeString += "'" + this.id + "',";
        writeString += "'" + this.title + "',";
        writeString += "'" + this.author + "',";
        writeString += this.quantity + ")";

        return writeString;
    }

    //Book to print string method returns a string of the book which can be displayed to the user
    public String toPrintString(){

        String printString = "\n ID:\t\t" + this.id;
        printString += "\n Title:\t\t" + this.title;
        printString += "\n Author:\t" + this.author;
        printString += "\n Quantity:\t" + this.quantity + "\n----------\n"; 

        return printString;
    }
}