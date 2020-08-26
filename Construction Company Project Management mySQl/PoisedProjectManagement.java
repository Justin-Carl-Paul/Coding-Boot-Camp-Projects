import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PoisedProjectManagement {

    // add new Project Function uses the Project class constructor to create a new project object
    // The project object is then written to the projects table in the poised_projects mySQL database
    public static void addNewProject(Statement statement, Scanner scanner){

        Project project = new Project(scanner);

        try{
            statement.executeUpdate(project.toWriteString("projects"));
        }
        catch(SQLException e){          
            e.printStackTrace();
        }

        System.out.println("Succesfully added " + project.title + " to the project list\n");
    }

    // Get project list function reads the project objects from the projects table
    // depending on the instruction given to the function the function will get different projects from the table
    // the function then returns a Project ArrayList
    public static ArrayList<Project> getProjectList(Statement statement, String instruction) {

        ArrayList<Project> projectList = new ArrayList<Project>();
        ResultSet results = null;

        try{
            results = statement.executeQuery(instruction);

            while(results.next()){
                Project project = new Project(results);
                projectList.add(project);
            }
        }
        catch(SQLException e){
            System.out.println("ERROR - Project(s) not found");
        }
        if(projectList.isEmpty()){

            System.out.println("No projects to display.");
        }

        return projectList;
    }

    //print Project List function prints the list that is passed to it to the screen for the user to view
    public static void printProjectList(ArrayList<Project> projectList){

        for(Project project : projectList){
            System.out.println(project.toPrintString());
        }
    }

    //Main Menu Function displays the main functions of the applications and requests that the user choose an option
    //The main menu then calls the approriate functions based on the users input
    public static void mainMenu(Statement statement) throws FileNotFoundException{

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Please select one of the following options:\n cp - Create a new project \n da - Display all projects \n di - Display incomplete projects");
            System.out.print(" do - Display overdue projects \n ep - Edit Project Details / Finalise Project \n ex - Exit the program\n");
            String userSelection = scanner.nextLine();
            
            if(userSelection.equals("cp")){
                addNewProject(statement, scanner);
            }
            else if(userSelection.equals("da")){
                printProjectList(getProjectList(statement, "SELECT * FROM projects"));
            }
            else if(userSelection.equals("di")){
                printProjectList(getProjectList(statement, "SELECT * FROM projects WHERE completedDate = 'Active'"));
            }
            else if(userSelection.equals("do")){
                printProjectList(getProjectList(statement, "SELECT * FROM projects WHERE completedDate = 'Active' AND deadLine < CURRENT_DATE()"));
            }
            else if(userSelection.equals("ep")){
                editProjectMenu(statement, scanner);
            }
            else if(userSelection.equals("ex")){
                break;
            }
            else{
                System.out.println("ERROR: Input not recognized please try again");
            }
        }

        scanner.close();
    }

    //Edit Project MENU function gets a project object based on the users input
    //The user is asked if they want to search for the project using the project number or project title
    //The user is then asked which action they would like to perform on the project object
    //The function calls the correct functions on the project object depending on the users input
    public static void editProjectMenu(Statement statement, Scanner scanner){

        ResultSet results = null;
        String sqlInput = "";

        //Asks the user if they want to search for the project with the project number or project title and then fetches that project
        //from the mySQL table if it exists. If the project does not exist the user is notified and is sent back to the main menu.
        while(true){
            System.out.println("How would you like to search for the project?: \n sn - Search by project number \n st - search by project title");
            String userSelection = scanner.nextLine();
    
            if(userSelection.equals("st")){
                System.out.println("Enter the title of the project you are looking for: ");
                String projectTitle = scanner.nextLine();
                sqlInput = "SELECT * FROM projects WHERE title = '" + projectTitle + "'";
                break;
            }
            else if(userSelection.equals("sn")){
                System.out.println("Enter the number of the project you are looking for: ");
                String projectNumber = scanner.nextLine();
                sqlInput = "SELECT * FROM projects WHERE number = '" + projectNumber + "'";
                break;
            }
            else{
                System.out.println("ERROR - Input not recognized please try again");
            }
        }

        try{

            //populates the resultSet variable with the project data
            results = statement.executeQuery(sqlInput);

            //checks if the results variable returned a project object. If not then the user is sent back to the main menu.
            if(results.next()){
                Project project = new Project(results);

                //submenu which asks the user how they would like to edit the selected project.
                //The user can make as many changes as they like. only by selecting ex are they sent back to the main menu.
                while(true){
                    System.out.println("Please select one of the following options:\n cd  - Change the project deadline \n cf  - Change the total amount of the fee paid to date");
                    System.out.print(" cc  - Update the contractors contact details \n ca  - Update the architects contact details \n ccu - Update the customers contact details");
                    System.out.print("\n fp  - Finalise a Project \n ex  - Exit back to the Main Menu and save project changes\n");
                    String userSelection = scanner.nextLine();
        
                    if(userSelection.equals("cd")){
                        editDeadLine(project, statement, scanner);
                    }
                    else if(userSelection.equals("cf")){
                        editAmountPaidToDate(project, statement, scanner);
                    }
                    else if(userSelection.equals("cc")){
                        editPerson(project, "contractor", scanner, statement);
                    }
                    else if(userSelection.equals("ca")){
                        editPerson(project, "architect", scanner, statement);
                    }
                    else if(userSelection.equals("ccu")){
                        editPerson(project, "customer", scanner, statement);
                    }
                    else if(userSelection.equals("fp")){
                        System.out.println(project.finalised);
                        project.finalise(statement);
                        break;
                    }
                    else if(userSelection.equals("ex")){
                        break;
                    }
                    else{
                        System.out.println("ERROR: Input not accepted please try again");
                    }
                }
            }
            else{
                System.out.println("SQL ERROR - Project not found");
            }
        }
        catch(SQLException e){
            System.out.println("SQL ERROR - Project not found");
        }
    }

     //Edits the deadline of the project that is passed to it inb the projects table
     //If the selected project has been finalised the user is notified that they cannot edit the deadline of a finalised project   
    public static void editDeadLine(Project project, Statement statement, Scanner scanner){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        if(project.finalised == 1){
            System.out.println("ERROR - Cannot edit finalised project dead line");
        }
        else{
            project.changeDeadLine(scanner);
            try{
                statement.executeUpdate("UPDATE projects SET deadLine = '" + dateFormat.format(project.deadLine) + "' WHERE number = '" + project.number + "';");
                System.out.println("Project " + project.number + " dead line updated to: " + dateFormat.format(project.deadLine));
            }
            catch(SQLException e){
                System.out.println("ERROR - Cannot Update the dead line ");
            }
        }
    }

    //This function changes the amount paid to date of the project in the projects table as well as the projects_finalised table
    //if the project has been finalised
    public static void editAmountPaidToDate(Project project, Statement statement, Scanner scanner){
        
        try{
            if(project.finalised == 1){
                statement.executeUpdate("UPDATE projects SET amountPaidToDate = '" + project.changeAmountPaidToDate(scanner) + "' WHERE number = '" + project.number + "';");
                statement.executeUpdate("UPDATE projects SET outstandingAmount = '" + project.outstandingAmount + "' WHERE number = '" + project.number + "';");
                statement.executeUpdate("UPDATE finalised_projects SET amountPaidToDate = '" + project.amountPaidToDate + "' WHERE number = '" + project.number + "';");
                statement.executeUpdate("UPDATE finalised_projects SET outstandingAmount = '" + project.outstandingAmount + "' WHERE number = '" + project.number + "';");
                System.out.println("Project " + project.number + " amount paid to date updated to:  " + project.amountPaidToDate + ".");
            }
            else if(project.finalised == 0){
                statement.executeUpdate("UPDATE projects SET amountPaidToDate = '" + project.changeAmountPaidToDate(scanner) + "' WHERE number = '" + project.number + "';");
                statement.executeUpdate("UPDATE projects SET outstandingAmount = '" + project.outstandingAmount + "' WHERE number = '" + project.number + "';");
                System.out.println("Project " + project.number + " amount paid to date updated to:  " + project.amountPaidToDate + ".");
            }
        }
        catch(SQLException e){
            System.out.println("ERROR - Cannot Update the dead line ");
        }
    }
    
    public static void editPerson(Project project, String person, Scanner scanner, Statement statement){

        ArrayList<String> coloumnName = new ArrayList<String>();
        coloumnName.add("cellNumber");
        coloumnName.add("emailAdress");
        coloumnName.add("physicalAdress");

        for(int i = 0; i < 3; i++){
            System.out.println("Enter the " + person + " " + coloumnName.get(i) + ": ");
            String userInput = scanner.nextLine();
            try{
                if(project.finalised == 1){
                    statement.executeUpdate("UPDATE projects SET  " + person + coloumnName.get(i) + "= '" + userInput + "' WHERE number = '" + project.number + "'");
                    statement.executeUpdate("UPDATE finalised_projects SET  " + person + coloumnName.get(i) + "= '" + userInput + "' WHERE number = '" + project.number + "'");
                    System.out.println("Project " + project.number + " " + person + " contact details updated.");
                }
                if(project.finalised == 0){
                    statement.executeUpdate("UPDATE projects SET  " + person + coloumnName.get(i) + "= '" + userInput + "' WHERE number = '" + project.number + "'");
                    System.out.println("Project " + project.number + " " + person + " contact details updated.");
                }
            }
            catch(SQLException e){
                System.out.println("ERROR - Cannot Update " + person + " " + coloumnName.get(i));
            }
        }
    }

    //The main body of the application which calls the main menu function and creates the
    //connection to the mySQL server so that the 
    public static void main(String args[]) throws FileNotFoundException{

        try {
            // Connect to the library_db database, via the jdbc:mysql: channel on localhost (this PC)
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poised_projects?useSSL=false",
                    "root",
                    "Ironhorse1!"
                    );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            //The main menu function is called
            mainMenu(statement); 

            statement.close();
            connection.close();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}