import java.util.*;
import java.io.*;

public class PoisedProjectManagement {

    // add new Project Function uses the Project class constructor to create a new project object
    // The project object is then written to the project.txt file
    public static void addNewProject(String projectFileName) throws FileNotFoundException {

        ArrayList<Project> projectList = new ArrayList<Project>();
        projectList.addAll(getProjectList(projectFileName));

        Scanner scanner = new Scanner(System.in);
        Project project = new Project(scanner, projectList);

        projectList.add(project);
        writeProjectList(projectList, projectFileName);

        System.out.println("Succesfully added " + project.title + " to the project list\n");
    }

    // Get project list function reads all of the project objects from the project.txt file
    // the function then returns a Project ArrayList from the text file data
    public static ArrayList<Project> getProjectList(String projectFileName) throws FileNotFoundException {

        ArrayList<Project> projectList = new ArrayList<Project>();

        File projectFile = new File(projectFileName);
        Scanner projectScanner = new Scanner(projectFile).useDelimiter("\\n");

        while (projectScanner.hasNextLine()) {

            try {

                projectScanner.nextLine();
                Project project = new Project().fromFileConstructor(projectScanner);
                projectList.add(project);

            } catch (NoSuchElementException e) {

                System.out.println("ERROR - Reading from project file");
                break;
            }
        }

        projectScanner.close();
        return projectList;
    }

    // Get active project list function returns a list of projects which are still active and have not been finalised yet
    public static ArrayList<Project> getActiveProjectList(String projectFileName) throws FileNotFoundException {

        ArrayList<Project> projectList = new ArrayList<Project>();

        for (Project project : getProjectList(projectFileName)) {

            if (project.finalised == false) {

                projectList.add(project);
            }
        }

        if(projectList.isEmpty()){

            System.out.println("No active projects to display.\n");
        }

        return projectList;
    }

    // Get past due date project list returns a list of all the projects that are overdue
    public static ArrayList<Project> getOverdueProjectList(String projectFileName) throws FileNotFoundException {

        ArrayList<Project> projectList = new ArrayList<Project>();          
        Date today = Calendar.getInstance().getTime();

        for (Project project : getActiveProjectList(projectFileName)) {

            if(project.deadLine.compareTo(today) < 1){

                projectList.add(project);
            }
        }

        if(projectList.isEmpty()){

            System.out.println("No overdue projects to display.\n");
        }

        return projectList;
    }

    //Write Project List function writes an ArrayList of Projects to a text file
    public static void writeProjectList(ArrayList<Project> projectList, String projectFileName){

        try{

            FileWriter projectFile = new FileWriter(projectFileName);
            PrintWriter projectWriter = new PrintWriter(projectFile);

            for(Project project : projectList){

                projectWriter.write(project.toWriteString());
            }

            projectWriter.close();
        }

        catch(IOException e){

            System.out.println("ERROR - Writing project to file");
        }
    }

    //print Project List function prints the list that is passed to it to the screen
    public static void printProjectList(ArrayList<Project> projectList){

        for(Project project : projectList){

            System.out.println(project.toPrintString());
        }
    }

    //Main Menu Function displays the main functions of the applications and requests that the user choose an option
    //The main menu then calls the apprpriate functions based on the users input
    public static void mainMenu(String projectFileName, String completedProjectFileName) throws FileNotFoundException{

        Scanner mainMenuScanner = new Scanner(System.in);

        while(true){

            System.out.println("Please select one of the following options:\n cp - Create a new project \n da - Display all projects \n di - Display incomplete projects");
            System.out.print(" do - Display overdue projects \n ep - Edit Project Details / Finalise Project \n ex - Exit the program\n");
            String userSelection = mainMenuScanner.nextLine();
            
            if(userSelection.equals("cp")){
                
                addNewProject(projectFileName);
            }
            else if(userSelection.equals("da")){
                
                printProjectList(getProjectList(projectFileName));
            }
            else if(userSelection.equals("di")){
                
                printProjectList(getActiveProjectList(projectFileName));
            }
            else if(userSelection.equals("do")){
                
                printProjectList(getOverdueProjectList(projectFileName));
            }
            else if(userSelection.equals("ep")){
                editProjectMenu(projectFileName, completedProjectFileName);
            }
            else if(userSelection.equals("ex")){
                break;
            }
            else{
                System.out.println("ERROR: Input not accepted please try again");
            }
        }

        mainMenuScanner.close();
    }

    //Edit Project MENU function gets a project object based on the users input
    //The user is then asked which action they would like to perform on the project object
    //The function calls the correct methods depending on the users input
    public static void editProjectMenu(String projectFileName, String completedProjectFileName)throws FileNotFoundException{

        ArrayList<Project> projectList = new ArrayList<Project>();
        projectList.addAll(getProjectList(projectFileName));
        Project selectedProject = new Project();
        Boolean projectCheck = false;

        System.out.println("Enter the number or the name the project you want to edit/finalise: ");
        Scanner projectScanner = new Scanner(System.in);
        String projectNumName = projectScanner.nextLine();

        //Runs through the projects in the project lists and gets the project that the user is looking for
        for(Project project : projectList){

            if(projectNumName.equals(project.number) || (projectNumName.equals(project.title))){

                selectedProject = project;
                projectCheck = true;
                break;
            }
        }

        if(projectCheck == false){

            System.out.println("ERROR - Project number/name not found!\n");
        }
        else if(selectedProject.finalised == true){

            System.out.println("ERROR - Cannot edit a finalised project!\n");
        }
        else{
            
            //Acts as the menu calling the correct functions for the users input
            while(projectCheck == true){

                System.out.println("Please select one of the following options:\n cd  - Change the project deadline \n cf  - Change the total amount of the fee paid to date");
                System.out.print(" cc  - Update the contractors contact details \n ca  - Update the architects contact details \n ccu - Update the customers contact details");
                System.out.print("\n fp  - Finalise a Project \n ex  - Exit back to the Main Menu and save project changes\n");
                String userSelection = projectScanner.nextLine();

                if(userSelection.equals("cd")){
                    
                    selectedProject.changeDueDate(projectScanner);
                }
                else if(userSelection.equals("cf")){

                    selectedProject.changeAmountPaidToDate(projectScanner);
                }
                else if(userSelection.equals("cc")){
                    
                    selectedProject.contractor.changePersonDetails(projectScanner);
                }
                else if(userSelection.equals("ca")){
                    
                    selectedProject.architect.changePersonDetails(projectScanner);
                }
                else if(userSelection.equals("ccu")){
                    
                    selectedProject.customer.changePersonDetails(projectScanner);
                }
                else if(userSelection.equals("fp")){
                    
                    selectedProject.finalise(completedProjectFileName);
                    projectCheck = false;
                }
                else if(userSelection.equals("ex")){
                    projectCheck = false;
                }
                else{
                    System.out.println("ERROR: Input not accepted please try again");
                }
            }

            //Writes the edited project back to the project.txt file essentially saving the changes
            writeProjectList(projectList, projectFileName);
            System.out.println("Changes Saved for project: " + selectedProject.title + "\n");
        }
    }

    //The main body of the application which calls the main menu function
    public static void main(String args[]) throws FileNotFoundException{

        String completedProjectFileName = "C:\\Users\\Justin\\Documents\\Hyperion Dev\\2 - Thinking like a software Engineer\\Task 24\\completedProject.txt";
        String projectFileName = "C:\\Users\\Justin\\Documents\\Hyperion Dev\\2 - Thinking like a software Engineer\\Task 24\\project.txt";
       
        mainMenu(projectFileName, completedProjectFileName);
    }
}