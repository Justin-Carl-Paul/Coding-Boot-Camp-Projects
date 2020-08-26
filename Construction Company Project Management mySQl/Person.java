import java.util.*;

public class Person {

    //Class Attributes
    String firstName;
    String lastName;
    String cellNumber;
    String emailAdress;
    String physicalAdress;

    public Person(){
        
    }

    //Class Constructor
    public Person(Scanner scanner){

        System.out.println("What is the persons first name?: ");
        this.firstName = scanner.nextLine();

        System.out.println("What is the persons last name?: ");
        this.lastName = scanner.nextLine();

        System.out.println("What is the persons cell number?: ");
        this.cellNumber = scanner.nextLine();

        System.out.println("What is the persons email adress?: ");
        this.emailAdress = scanner.nextLine();

        System.out.println("What is the persons physical adress?: ");
        this.physicalAdress = scanner.nextLine();
    }

    //The to writestring takes the person object and turns it into a string which can be saved in a text file
    //this string is not meant to be viewed by any one and is only for the computer
    public String toWriteString(){

        String personString = "'" + this.firstName + "',";
        personString += "'" + this.lastName +"',";
        personString += "'" + this.cellNumber +"',";
        personString += "'" + this.emailAdress +"',";
        personString += "'" + this.physicalAdress + "'";

        return personString;
    }

    //Creates a string which is written to the invoice
    public String generateInvoice(){

        String invoiceString = "\t\t_____Customer Details_____\n";
        invoiceString += "First Name:\t\t" + this.firstName + "\n";
        invoiceString += "Last Name:\t\t" + this.lastName + "\n";
        invoiceString += "Cell Number:\t" + this.cellNumber + "\n";
        invoiceString += "E-mail:\t\t\t" + this.emailAdress + "\n";
        invoiceString += "Adress:\t\t\t" + this.physicalAdress + "\n";

        return invoiceString;
    }
    
    //The change person details function updates the contact details of any person object
    public void changePersonDetails(Scanner projectScanner){

        System.out.println("What is the persons new cell number?: ");
        this.cellNumber = projectScanner.nextLine();


        System.out.println("What is the persons new email adress?: ");
        this.emailAdress = projectScanner.nextLine();

        System.out.println("What is the persons physical adress?: ");
        this.physicalAdress = projectScanner.nextLine();
    }
}