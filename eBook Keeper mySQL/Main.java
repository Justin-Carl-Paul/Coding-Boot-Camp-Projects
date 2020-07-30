import java.sql.*;
import java.util.*;

public class Main {
    
    //the main menu function loops through the main options of the program until the user chooses to exit
    public static void mainMenu(Statement statement){

        Scanner scanner = new Scanner(System.in);

        while(true){

            System.out.println("Enter the number of the option you want to select:");
            System.out.println("1. Enter Book \n2. Update Book \n3. Delete Book \n4. Search Books \n0. EXIT");
    
            String userSelection = scanner.nextLine();
    
            if(userSelection.equals("1")){
                
                enterBook(statement);
            }
            else if(userSelection.equals("2")){

                updateBookMenu(statement, subMenu(statement, "want to update."));
            }
            else if(userSelection.equals("3")){
                
                deleteBook(statement, subMenu(statement, "want to delete."));
            }
            else if(userSelection.equals("4")){
                
                subMenu(statement, "are looking for.");
            }
            else if(userSelection.equals("0")){
                
                System.out.println("Exiting program...");
                break;
            }
            else{
    
                System.out.println("ERROR - Selection not recognised please try again");
            }
        }
        scanner.close();
    }

    //The sub menu asks the user to select the field that they want to use to searhc for the book(s)
    public static ArrayList<Book> subMenu(Statement statement, String function){

        ArrayList<Book> bookList = new ArrayList<Book>();
        Scanner scanner = new Scanner(System.in);

        while(true){

            System.out.println("Enter the number next to the field you want to use to select the book(s): ");
            System.out.println("1. ID \n2. Title \n3. Author \n0. EXIT");
            String userSelection = scanner.nextLine();

            if(userSelection.equals("1")){
                bookList.addAll(searchForBook(statement, "id", function));
                break;
            }
            else if(userSelection.equals("2")){
                bookList.addAll(searchForBook(statement, "title", function));
                break;
            }
            else if(userSelection.equals("3")){
                bookList.addAll(searchForBook(statement, "author", function));
                break;
            }
            else if(userSelection.equals("0")){
                System.out.println("Exiting sub menu...");
                break;
            }
            else{
    
                System.out.println("ERROR - Selection not recognised");
            }
        }

        return bookList;
    }

    //The enter book function adds a new book to the SQL database and notifies the user that the process has been succesfull
    public static void enterBook(Statement statement){

        Scanner scanner = new Scanner(System.in);
        Book book = new Book(scanner);

        try{

        //Insert the book into the book table
        statement.executeUpdate(book.toWriteString());
        }
        catch(SQLException e){
                        
            e.printStackTrace();
        }

        System.out.println("Succesfully added " + book.quantity + " copies of " + book.title + " to the database.");
    }

    //The searchbook functions looks to see if the book(s) exist in the database. 
    //The selected books are then put into an ArrayList which the function returns.
    public static ArrayList<Book> searchForBook(Statement statement, String searchColoumn, String function){

        ArrayList<Book> bookList = new ArrayList<Book>();
        Scanner scanner = new Scanner(System.in);
        ResultSet results = null;


        System.out.println("Enter the "+ searchColoumn + " of the book you " + function);
        String userSelection = scanner.nextLine();

        //if statement which adds the single quotations for the varchar fields in the SQL table
        if(searchColoumn.equals("id") == false){

            userSelection = "'" + userSelection + "'";
        }

        try{

            System.out.println("SELECT id, title, author, quantity FROM book WHERE " + searchColoumn + " = " + userSelection);
            results = statement.executeQuery("SELECT id, title, author, quantity FROM book WHERE " + searchColoumn + " = " + userSelection);

            while(results.next()){
                Book book = new Book(results);
                bookList.add(book);
                System.out.println(book.toPrintString());
            }

            //If the bookList is empty then the search was not succesful. The user is notified of this and the function is exited.
            if(bookList.isEmpty()){

                System.out.println("ERROR - Book(s) not found");
                return bookList;
            }

        }
        catch(SQLException e){

            System.out.println("ERROR - Book(s) not found");
        }

        return bookList;
    }

    //The delete book function deletes book(s) from the SQL database
    public static void deleteBook(Statement statement, ArrayList<Book> bookList){

        for(Book book : bookList){

            try{

                //Deletes the book(s) from the table
                statement.executeUpdate("DELETE FROM book WHERE id = " + book.id);
                System.out.println("Succesfully deleted book(s)");
            }
            catch(SQLException e){

                System.out.println("ERROR - Could not delete book");
            }
        }
    }

    //The update book menu function asks the user which coloumn of the table they want to uppade and then
    //passes the appropriate information to the update book functio
    public static void updateBookMenu(Statement statement, ArrayList<Book> bookList){

        if(bookList.isEmpty()){
            return;
        }

        while(true){

            System.out.println("Enter the number of the coloumn you want to update: ");
            System.out.println("1. ID \n2. Title \n3. Author \n4. Quantity \n0. EXIT");
            Scanner scanner = new Scanner(System.in);
            String userSelection = scanner.nextLine();

            if(userSelection.equals("1")){

                //If and else statements that prevents the user from selecting two books and then making the id's of those books the same
                if(bookList.size() > 1){

                    System.out.println("ERROR - Cannot change the id of multiple books");
                    continue;
                }
                else{

                    updateBook(statement, bookList, "id");
                }
                break;
            }
            else if(userSelection.equals("2")){

                updateBook(statement, bookList, "title");
                break;
            }
            else if(userSelection.equals("3")){

                updateBook(statement, bookList, "author");
                break;
            }
            else if(userSelection.equals("4")){

                updateBook(statement, bookList, "quantity");
                break;
            }
            else if(userSelection.equals("0")){

                System.out.println("Exiting update menu...");
                break;
            }
            else{

                System.out.println("ERROR - input not recognized");
            }
        }
    }

    //The update book function updates the book(s) in the database based on the users input
    public static void updateBook(Statement statement, ArrayList<Book> bookList, String updateColoumn){

        try{

            System.out.println("Enter the new book " + updateColoumn + ": ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();

            if(updateColoumn.equals("id") == false){

                userInput = "'" + userInput + "'";
            }

            for(Book book : bookList){

                //Updates the book(s) in the SQL database
                statement.executeUpdate("UPDATE book SET " + updateColoumn + " = " + userInput + " WHERE id = " + book.id);
            }

            System.out.println("Book(s) succesfully updated");
        }
        catch(SQLException e){

            System.out.println("ERROR - Could not update book");
        }
    }

    public static void main(String args[]){

        try {
            // Connect to the library_db database, via the jdbc:mysql: channel on localhost (this PC)
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ebookstore_db?useSSL=false",
                    "root",
                    "Ironhorse1!"
                    );
            // Create a direct line to the database for running our queries
            Statement statement = connection.createStatement();

            mainMenu(statement); 

            statement.close();
            connection.close();
            
        } 
        catch (SQLException e) {

            e.printStackTrace();
        }

        
    }
}