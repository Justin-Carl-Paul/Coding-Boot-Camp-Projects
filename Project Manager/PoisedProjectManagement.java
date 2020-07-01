import java.util.*;
import java.io.*;

public class PoisedProjectManagement {

    //New Project Function
    public static void inputNewProject(){

        Project newProject = new Project();

        Scanner numberScanner = new Scanner(System.in);
        System.out.println("Enter the project number:");
        int number = numberScanner.nextInt();

        Scanner titleScanner = new Scanner(System.in);
        System.out.println("Enter the project title or leave blank for default project title:");
        String title = titleScanner.nextLine();

        Scanner buildingTypeScanner = new Scanner(System.in);
        System.out.println("Enter the building type:");
        String buildingType = buildingTypeScanner.nextLine();

        Scanner adressScanner = new Scanner(System.in);
        System.out.println("Enter the building type:");
        String adress = adressTypeScanner.nextLine();

        Scanner erfNumberScanner = new Scanner(System.in);
        System.out.println("Enter the building type:");
        String erfNumber = erfNumberTypeScanner.nextLine();

        Scanner totalFeeScanner = new Scanner(System.in);
        System.out.println("Enter the building type:");
        float totalFee = totalFeeTypeScanner.nextFloat();

        Scanner deadlineScanner = new Scanner(System.in);
        System.out.println("Enter the building type:");
        String deadline = deadlineScanner.nextFloat();

        // Scanner architectScanner = new Scanner(System.in);
        // System.out.println("Enter the building type:");
        // String architect = architectScanner.nextFloat();

        // Scanner contractorScanner = new Scanner(System.in);
        // System.out.println("Enter the building type:");
        // String contractor = contractorScanner.nextFloat();

        // Scanner customerScanner = new Scanner(System.in);
        // System.out.println("Enter the building type:");
        // String customer = customerScanner.nextFloat();

    }

    //Write Architect Function
    public static void writeArchitect(){

        FileWriter architectWriter = new FileWriter("architect.txt");

        architectWriter.write("Hi there");
        architectWriter.close();

    }

    //New Achitect Function
    public static void inputNewArchitect(){

        Scanner firstNameScanner = new Scanner(System.in);
        System.out.println("Enter the architects first name:");
        String firstName = firstNameScanner.nextLine();

        Scanner lastNameScanner = new Scanner(System.in);
        System.out.println("Enter the architects last name");
        String lastName = lastNameScanner.nextLine();

        Scanner telephoneNumberScanner = new Scanner(System.in);
        System.out.println("Enter the architects phone number:");
        String telephoneNumber = telephoneNumberScanner.nextLine();

        Scanner emailAdressScanner = new Scanner(System.in);
        System.out.println("Enter architects emaial adress:");
        String emailAdress = emailAdressScanner.nextLine();

        Scanner physicalAdressScanner = new Scanner(System.in);
        System.out.println("Enter the architects physical adress:");
        String physicalAdress = physicalAdressScanner.nextLine();

        Architect architect = new Architect(firstName, lastName, telephoneNumber, emailAdress, physicalAdress);
    }

    //Main Menu Function
    public static void mainMenu(){

        boolean mainMenuCheck = true;

        while(mainMenuCheck == true){

            Scanner Scanner = new Scanner(System.in);
            System.out.println("Welcome to the main menu. Please select one of the following options:\n cp - Create a new project \n ep - Edit Project Details \n cb - Create a new Adress Book Entry \n eb - Edit Adress Book \n fp - Finalise a project \n ex - Exit The Program");
            String userSelection = Scanner.nextLine();

            if(userSelection.equals("cp")){
                inputNewProject();
            }
            else if(userSelection.equals("ep")){
                
            }
            else if(userSelection.equals("cb")){
                createNewAdressBookEntryMenu();
            }
            else if(userSelection.equals("eb")){
                
            }
            else if(userSelection.equals("fp")){
                
            }
            else if(userSelection.equals("ex")){
                mainMenuCheck = false;
            }
            else{
                System.out.println("ERROR: Input not accepted please try again");
            }
        }
    }

    //Create new adress book entry function
    public static void createNewAdressBookEntryMenu(){

        boolean menuCheck = true;

        while(menuCheck == true){

            Scanner Scanner = new Scanner(System.in);
            System.out.println("Create New Adress Book Entry Menu: \n ar - Add a new architect \n cu - Add a new customer \n co - Add a new contractor \n ex - Exit back to main menu ");
            String userSelection = Scanner.nextLine();

            if(userSelection.equals("ar")){
                inputNewArchitect();
            }
            else if(userSelection.equals("cu")){

            }
            else if(userSelection.equals("co")){

            }
            else if(userSelection.equals("ex")){
                menuCheck = false;
            }
            else{
                System.out.println("ERROR: Input not accepted please try again");
            }
        }

    }

    public static void main(String args[]) throws IOException{


        Architect architect = new Architect("John", "Doe", "0832557884", "jd@gmail.com", "435 My Road");
        
        File file = new File("architect.txt");
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        
        pw.println("Example text");

        pw.close();

        System.out.println(architect.emailAdress);
    }
}