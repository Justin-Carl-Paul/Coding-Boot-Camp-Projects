import java.util.*;

public class Person {

    //Class Attributes
    String firstName;
    String lastName;
    String cellNumber;
    String emailAdress;
    String physicalAdress;

    //Class Constructor
    public Person(Scanner scanner, Boolean print ){

        if(print) System.out.println("What is the persons first name?: ");
        this.firstName = stringEmptyCatch(scanner);

        if(print) System.out.println("What is the persons last name?: ");
        this.lastName = stringEmptyCatch(scanner);

        if(print) System.out.println("What is the persons cell number?: ");
        this.cellNumber = stringEmptyCatch(scanner);

        if(print) System.out.println("What is the persons email adress?: ");
        this.emailAdress = stringEmptyCatch(scanner);

        if(print) System.out.println("What is the persons physical adress?: ");
        this.physicalAdress = stringEmptyCatch(scanner);
    }

    //The to writestring takes the person object and turns it into a string which can be saved in a text file
    //this string is not meant to be viewed by any one and is only for the computer
    public String toWriteString(){

        String personString = this.firstName +"\n";
        personString += this.lastName +"\n";
        personString += this.cellNumber +"\n";
        personString += this.emailAdress +"\n";
        personString += this.physicalAdress;

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

    //The string empty catch function repeats a while loop until the user enters a string value that is not blank
    public String stringEmptyCatch(Scanner scanner){

        String userInput = "";

        while(true){

            userInput = scanner.nextLine();

            if(userInput.isBlank()){

                System.out.println("ERROR - This field cannot be empty");
            }
            else{

                break;
            }
        }

        return userInput;
    }
}