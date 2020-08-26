import java.util.*;
import java.util.Date;
import java.io.*;
import java.sql.*;
import java.text.*;

public class Project {
    //Class attributes
    String number;
    String title;
    String buildingType;
    String adress;
    String erfNumber;
    float totalFee;
    float amountPaidToDate;
    float outstandingAmount;
    Date deadLine;
    String completedDate;
    int finalised;
    Person architect;
    Person contractor;
    Person customer;

    //Class default constructor
    public Project(){

        this.customer = new Person();
        this.architect = new Person();
        this.contractor= new Person();
    }

    //Class constructor that is used for reading data from the user inputs using a Scanner object
    public Project(Scanner scanner){
     
        System.out.println("Enter the project number:");
        this.number = scanner.nextLine();

        System.out.println("Enter the project title or leave blank for default project title:");
        this.title = scanner.nextLine();
        
        System.out.println("Enter the building type:");
        this.buildingType = stringEmptyCatch(scanner);

        System.out.println("Enter the building adress:");
        this.adress = stringEmptyCatch(scanner);

        System.out.println("Enter the building erf number:");
        this.erfNumber = stringEmptyCatch(scanner);

        System.out.println("Enter the total fee of the project:");
        this.totalFee = floatTryCatch(scanner, true);
        
        System.out.println("How much money has been paid to date?:");
        this.amountPaidToDate = floatTryCatch(scanner, true);

        this.outstandingAmount = totalFee - amountPaidToDate;
        
        System.out.println("Enter the project deadline(yyyy-mm-dd):");
        this.deadLine = dateTryCatch(scanner, true);

        this.completedDate = "Active";

        this.finalised = 0; //0 =false

        System.out.println("Enter the details for the CUSTOMER: ");
        this.customer = new Person(scanner);

        System.out.println("Enter the details for the ARCHITECT: ");
        this.architect = new Person(scanner);

        System.out.println("Enter the details for the CONTRACTOR: ");
        this.contractor = new Person(scanner);

        //If there is no input in the title field then a title is generated from the building type and customer last name
        if(this.title.isEmpty()){
            this.title = buildingType + " " + customer.lastName;
        }
    }

    //class constructor used for reading data from a ResultSet
    public Project(ResultSet results){

        try{

            this.number = results.getString(1);
            this.title = results.getString(2);
            this.buildingType = results.getString(3);
            this.adress = results.getString(4);
            this.erfNumber = results.getString(5);
            this.totalFee = results.getFloat(6);
            this.amountPaidToDate = results.getFloat(7);
            this.outstandingAmount = results.getFloat(8);
            this.deadLine = results.getDate(9);
            this.completedDate = results.getString(10);      
            this.finalised = results.getInt(11);

            this.customer = new Person();
            this.customer.firstName = results.getString(12);
            this.customer.lastName = results.getString(13);
            this.customer.cellNumber = results.getString(14);
            this.customer.emailAdress = results.getString(15);
            this.customer.physicalAdress = results.getString(16);

            this.architect = new Person();
            this.architect.firstName = results.getString(17);
            this.architect.lastName = results.getString(18);
            this.architect.cellNumber = results.getString(19);
            this.architect.emailAdress = results.getString(20);
            this.architect.physicalAdress = results.getString(21);

            this.contractor= new Person();
            this.contractor.firstName = results.getString(22);
            this.contractor.lastName = results.getString(23);
            this.contractor.cellNumber = results.getString(24);
            this.contractor.emailAdress = results.getString(25);
            this.contractor.physicalAdress = results.getString(26);
        }
        catch(final SQLException e){
            e.printStackTrace();
        }
    }

    //The to writestring takes the project object and turns it into a string which can be written to mySQL table
    //this string is not meant to be viewed by any one and is only for java and mySQL
    public String toWriteString(String tableName){

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String projectString = "INSERT INTO " + tableName + " VALUES(";
        projectString +="'" + this.number + "',";
        projectString += "'" + this.title + "',";
        projectString += "'" + this.buildingType + "',";
        projectString += "'" + this.adress + "',";
        projectString += "'" + this.erfNumber + "',";
        projectString += "'" + this.totalFee + "',";
        projectString += "'" + this.amountPaidToDate + "',";
        projectString += "'" + this.outstandingAmount + "',";
        projectString += "'" + dateFormat.format(this.deadLine) + "',";
        projectString += "'" + this.completedDate + "',";
        projectString += "'" + this.finalised + "',";
        projectString += this.customer.toWriteString() + ",";
        projectString += this.architect.toWriteString() + ",";
        projectString += this.contractor.toWriteString() + ")";

        return projectString;
    }

    //The to print string function returns a string which contains all the project details displayed in a user friendly easy to read manner
    public String toPrintString(){
        
        String projectString ="\n\t_____PROJECT: " + this.number + "_____\n";
        projectString += "Title:\t\t\t" + this.title + "\n";
        projectString += "Building Type:\t\t" + this.buildingType + "\n";
        projectString += "Adress:\t\t\t" + this.adress + "\n";
        projectString += "Erf Number:\t\t" + this.erfNumber + "\n";
        projectString += "Total Cost:\t\tR " + this.totalFee + "\n";
        projectString += "Amount Paid:\t\tR " + this.amountPaidToDate + "\n";
        projectString += "Oustanding Amount:\tR " + this.outstandingAmount + "\n";
        projectString += "Dead Line:\t\t" + this.deadLine + "\n";
        projectString += "Customer:\t" + this.customer.firstName + " " + this.customer.lastName +  "\n";
        projectString += "Architect:\t" + this.architect.firstName + " " + this.architect.lastName +  "\n";
        projectString += "Contactor:\t" + this.contractor.firstName + " " + this.contractor.lastName +  "\n";

        return projectString;
    }

    //The generate invoice method creates new text file with a unique name and writes certain project details to the new text file
    //The invoice is written in a text file so that it can be sent to the customer via email
    public void generateInvoice(){

        String invoiceString = "_____Invoice for " + this.title + "_____\n\n";
        invoiceString += this.customer.generateInvoice() + "\n";
        invoiceString += "Balance Due: R" + this.outstandingAmount + "\n";

        try{
            final FileWriter projectWriter = new FileWriter("C:\\Users\\Justin\\Documents\\Hyperion Dev\\3 - Advanced Software Engineering\\Task 8\\Capstone Project I" + this.title + ".txt");
            projectWriter.write(invoiceString);
            projectWriter.close();
        }
        catch(final IOException e){
            System.out.println("Error writing invoice");
        }
    }

    //The finalise method sets the finalised attribute to true the completed date is also added as the date that the project is finalised
    //The project is saved to the finalised_projects table
    public void finalise(Statement statement){

        if(this.finalised == 0){
            Date today = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
            this.finalised = 1;
            this.completedDate = dateFormat.format(today);
            
            try{
                statement.executeUpdate("UPDATE projects SET finalised = '" + this.finalised + "'  WHERE number = '" + this.number + "'");
                statement.executeUpdate("UPDATE projects SET completedDate = '" + this.completedDate + "'  WHERE number = '" + this.number + "'");
            }
            catch(SQLException e){
                System.out.println("ERROR - Cannot finalise project");
            }
            
            //checks if the outstanding amount is not zero. If the customer still owes money on the project then a invoice is generated
            //The invoice is written to a .txt file so that it can be sent to the customer
            if(this.outstandingAmount != 0){
                this.generateInvoice();
                System.out.println("Invoice Generated, outstanding amount owed by customer = R " + this.outstandingAmount);
            }
    
            //The completed project is then added to the finalised_projects table
            try{
                statement.executeUpdate(this.toWriteString("finalised_projects"));
            }
            catch(SQLException e){          
                e.printStackTrace();
            }
        }
        else{
            System.out.println("ERROR - Cannot edit a finalised project");
        }
    }

    //The change due date method updates the dead line of the project object
    public Date changeDeadLine(Scanner scanner){

        System.out.println("Enter the new dead line (yyyy-mm-dd): ");
        
        return this.deadLine = dateTryCatch(scanner, true);
    }

    //the change amount paid to date method updates the amoun paid to date for the project object
    //The new outstanding amount is then calculated
    public float changeAmountPaidToDate(Scanner projectScanner){

        System.out.println("Enter the total amount that has been paid to date: ");
        this.amountPaidToDate = floatTryCatch(projectScanner, true);

        this.outstandingAmount = this.totalFee-this.amountPaidToDate;

        return this.amountPaidToDate;
    }

    //Float input try catch function catches incorrect float values that are inputted and notifes the user prompting them to enter the correct value
    //This is looped until the user enters the correct value. When reading from a text file the function is not looped
    public float floatTryCatch(final Scanner scanner, final Boolean loopCheck){

        float floatInput = 0;

        do{
            try{
                floatInput = Float.parseFloat(scanner.nextLine());
                break;
            }
            catch(final NumberFormatException  e){
                System.out.println("ERROR - The value entered is not a FLOAT value");
                continue;
            }
        }
        while(loopCheck);

        return floatInput;
    }

    //Date input try catch function catches incorrect date values that are inputted and notifes the user prompting them to enter the correct value
    //This is looped until the user enters the correct value. When reading from a text file the function is not looped
    public Date dateTryCatch(Scanner scanner, Boolean loopCheck){

        Date dateInput = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        do{
            try {
                dateInput = dateFormat.parse(scanner.nextLine());
                break;
            }
            catch (final ParseException e) {
                System.out.println("ERROR - The Date value entered is not in the correct format");
                continue;
            }
        }
        while(loopCheck);

        return dateInput;
    }

    //Boolean input try catch function catches incorrect boolean values that are inputted and notifes the user prompting them to enter the correct value
    //This is looped until the user enters the correct value. When reading from a text file the function is not looped
    public Boolean booleanTryCatch(final Scanner scanner, final Boolean loopCheck){

        Boolean booleanInput = false;

        do{
            try {
                booleanInput = Boolean.parseBoolean(scanner.nextLine());
                break;
            }
            catch (final InputMismatchException e) {
                System.out.println("ERROR - The value entered is not a Boolean value");
                continue;
            }
        }
        while(loopCheck);

        return booleanInput;
    }

    //The string empty catch function repeats a while loop until the user enters a string value that is not blank
    public String stringEmptyCatch(final Scanner scanner){

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