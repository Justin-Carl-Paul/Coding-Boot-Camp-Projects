import java.util.*;
import java.io.*;
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
    Boolean finalised;
    Person architect;
    Person contractor;
    Person customer;

    //Class default constructor
    public Project(){

    }

    //Class constructor that is used for reading data from the user inputs using a Scanner object
    public Project(Scanner scanner, ArrayList<Project> projectList){

        //Ensures that the user does not enter a project number that already exists
        //The loop is run until the user enters a project number which does not already exist
        while(true){
            Boolean loopCheck = true;
            System.out.println("Enter the project number:");
            this.number = scanner.nextLine();

            for(Project project : projectList){

                if(this.number.equals(project.number)){
                    System.out.println("Project number already exists. Enter a new project number");
                    loopCheck = false;
                    continue;
                }
            }

            if(loopCheck){
                break;
            }
        }

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
        
        System.out.println("Enter the project deadline(dd/mm/yyyy):");
        this.deadLine = dateTryCatch(scanner, true);

        this.completedDate = "Active";

        this.finalised = false;

        System.out.println("Enter the details for the _CUSTOMER_");
        this.customer = new Person(scanner, true);

        System.out.println("Enter the details for the _ARCHITECT_");
        this.architect = new Person(scanner, true);

        System.out.println("Enter the details for the _CONTRACTOR_");
        this.contractor = new Person(scanner, true);

        //If there is no input in the title field then a title is generated from the building type and customer last name
        if(this.title.isEmpty()){

            this.title = buildingType + " " + customer.lastName;
        }
    }

    //'pseudo' class constructor method which is used for reading data from .txt files with a Scanner object
    public Project fromFileConstructor(Scanner scanner){

        this.number = scanner.nextLine();
        this.title = scanner.nextLine();
        this.buildingType = scanner.nextLine();
        this.adress = scanner.nextLine();
        this.erfNumber = scanner.nextLine();
        this.totalFee = floatTryCatch(scanner, false);
        this.amountPaidToDate = floatTryCatch(scanner, false);
        this.outstandingAmount = floatTryCatch(scanner, false);
        this.deadLine = dateTryCatch(scanner, false);
        this.completedDate = scanner.nextLine();      
        this.finalised = booleanTryCatch(scanner, false);
        this.customer = new Person(scanner, false);
        this.architect = new Person(scanner, false);
        this.contractor = new Person(scanner, false);

        return this;
    }

    //The to writestring takes the project object and turns it into a string which can be saved in a text file
    //this string is not meant to be viewed by any one and is only for the computer
    public String toWriteString(){

        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

        String projectString ="\n" + this.number + "\n";
        projectString += this.title + "\n";
        projectString += this.buildingType + "\n";
        projectString += this.adress + "\n";
        projectString += this.erfNumber + "\n";
        projectString += this.totalFee + "\n";
        projectString += this.amountPaidToDate + "\n";
        projectString += this.outstandingAmount + "\n";
        projectString += dateFormat.format(this.deadLine) + "\n";
        projectString += this.completedDate + "\n";
        projectString += this.finalised + "\n";
        projectString += this.customer.toWriteString() + "\n";
        projectString += this.architect.toWriteString() + "\n";
        projectString += this.contractor.toWriteString() + "\n";

        return projectString;
    }

    //The to print string function returns a string which contains all the project details displayed in a user
    //friendly easy to read manner
    public String toPrintString(){

        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        
        String projectString ="\n\t_____PROJECT: " + this.number + "_____\n";
        projectString += "Title:\t\t\t" + this.title + "\n";
        projectString += "Building Type:\t\t" + this.buildingType + "\n";
        projectString += "Adress:\t\t\t" + this.adress + "\n";
        projectString += "Erf Number:\t\t" + this.erfNumber + "\n";
        projectString += "Total Cost:\t\tR " + this.totalFee + "\n";
        projectString += "Amount Paid:\t\tR " + this.amountPaidToDate + "\n";
        projectString += "Oustanding Amount:\tR " + this.outstandingAmount + "\n";
        projectString += "Due Date:\t\t" + dateFormat.format(this.deadLine) + "\n";
        projectString += "Customer:\t" + this.customer.firstName + " " + this.customer.lastName +  "\n";
        projectString += "Architect:\t" + this.architect.firstName + " " + this.architect.lastName +  "\n";
        projectString += "Contactor:\t" + this.contractor.firstName + " " + this.contractor.lastName +  "\n";

        return projectString;
    }

    //The generate invoice method creates new text file with a unique name and writes certain project details to the new text file
    public void generateInvoice(){

        String invoiceString = "_____Invoice for " + this.title + "_____\n\n";
        invoiceString += this.customer.generateInvoice() + "\n";
        invoiceString += "Balance Due: R" + this.outstandingAmount + "\n";

        try{

            FileWriter projectWriter = new FileWriter("C:\\Users\\Justin\\Documents\\Hyperion Dev\\2 - Thinking like a software Engineer\\Task 24\\invoice_" + this.title + ".txt");
            projectWriter.write(invoiceString);
            projectWriter.close();
        }
        catch(IOException e){

            System.out.println("Error writing invoice");
        }
    }

    //The finalise method sets the finalised attribute to true the completed date is also added as the date that the project is finalised
    public void finalise(String completedProjectFileName){

        this.finalised = true;

        Date today = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        this.completedDate = dateFormat.format(today);

        //checks if the outstanding amount is not zero. If the customer still owes money on the project then a invoice is generated
        if(this.outstandingAmount != 0){
            
            this.generateInvoice();
            System.out.println("Invoice Generated, outstanding amount owed by customer = R " + this.outstandingAmount);
        }

        //The completed project is then added to the completed project text file
        try{
            FileWriter projectFile = new FileWriter(completedProjectFileName, true);
            PrintWriter projectWriter = new PrintWriter(projectFile);
            projectWriter.write(this.toWriteString());
            projectWriter.close();

            System.out.println(this.title + " has been finalised.");
        }
        catch(IOException e){
    
                System.out.println("ERROR - writing finalised project");
        }
    }

    //The change due date method updates the due date of the project object
    public void changeDueDate(Scanner projectScanner){

        System.out.println("Enter the new deadline for the project(dd/mm/yyyy): ");
        this.deadLine = dateTryCatch(projectScanner, true);
    }

    //the change amount paid to date method updates the amoun paid to date for the project object
    //The new outstanding amount is then calculated
    public void changeAmountPaidToDate(Scanner projectScanner){

        System.out.println("Enter the total amount that has been paid to date: ");
        this.amountPaidToDate = floatTryCatch(projectScanner, true);

        this.outstandingAmount = this.totalFee-this.amountPaidToDate;
    }

    //Float input try catch function catches incorrect float values that are inputted and notifes the user prompting them to enter the correct value
    //This is looped until the user enters the correct value. When reading from a text file the function is not looped
    public float floatTryCatch(Scanner scanner, Boolean loopCheck){

        float floatInput = 0;

        do{
            try{
                floatInput = Float.parseFloat(scanner.nextLine());
                break;
            }
            catch(NumberFormatException  e){
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

        do{
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                dateInput = dateFormat.parse(scanner.nextLine());
                break;
            }
            catch (ParseException e) {
                System.out.println("ERROR - The Date value entered is not in the correct format");
                continue;
            }
        }
        while(loopCheck);

        return dateInput;
    }

    //Boolean input try catch function catches incorrect boolean values that are inputted and notifes the user prompting them to enter the correct value
    //This is looped until the user enters the correct value. When reading from a text file the function is not looped
    public Boolean booleanTryCatch(Scanner scanner, Boolean loopCheck){

        Boolean booleanInput = false;

        do{
            try {
                booleanInput = Boolean.parseBoolean(scanner.nextLine());
                break;
            }
            catch (InputMismatchException e) {
                System.out.println("ERROR - The value entered is not a Boolean value");
                continue;
            }
        }
        while(loopCheck);

        return booleanInput;
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