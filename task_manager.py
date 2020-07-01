from datetime import date
from datetime import datetime

#The getList function takes a ".txt" filename as the input.
#The function populates and then returns a two dimensional list with all of the necessary information 
def getList(fileName):

    string = ""
    listOneDimensional = []
    listTwoDimensional = []

    File = open(fileName,"r")

    for line in File:
        string += line

    if len(string) == 0:
        return listTwoDimensional

    listOneDimensional = string.split("\n") #splits the file into lines and populates a list with these values

    for i in range(0,len(listOneDimensional)):
        listTwoDimensional.append(listOneDimensional[i].split(", ")) #splits each new line element further
        listTwoDimensional[i].insert(0, i)

    return listTwoDimensional

#The getUserTasks function takes a specific userName and a ".txt" file name as the input
#The function creates and retruns a list which contains all of the tasks assigned to a specific user
def getUserTasks(userName,fileName):

    taskList = getList(fileName)
    userTaskList = []

    #checks which of the tasks are assigned to the username that was passed to the function
    for i in range(0,len(taskList)):
        if userName == taskList[i][1]:
            userTaskList.append(taskList[i])

    return userTaskList

#The writeToFile function takes a list and a ".txt" file names as inputs.
#the function writes the list that is passed to it to the ".txt" file that is passed to it
#the entire txt file is re-written each time. New data is not just appended to the text file.
def writeToFile(inputList, fileName):

    with open(fileName,"w") as f:

        for i, firstDimension in enumerate(inputList, 1):

            for j in range(1, len(firstDimension)):

                if j == (len(firstDimension)-1):
                    f.write(firstDimension[j])
                else:
                    f.write(firstDimension[j] + ", ")

            if i < len(inputList):  #Prevents empty lines from occouring at the end of the ".txt" file as this will cause issue when reading the file
                f.write("\n")

    f.close()

#The login function takes a ".txt" file as an input and also requests that the user enters a username and password.
#The function checks if the entered username and password exist in the ".txt" file that was passed to it
#if the user exists the function returns the username and the password that was used to log in
def login(fileName):

    loginCheck = True
    userNameList = getList(fileName)
    userNamePositionInList = -1
    userName = ""
    password = ""

    while loginCheck:

        userName = input("Please enter your username: ")
        password = input("Please enter your password: ")

        for i in range(0,len(userNameList)):
            if userNameList[i][1] == userName.strip():  #checks if the entered username exists in the ".txt" file and stores the psotition in the list if it does
                userNamePositionInList = i
            
        if password == userNameList[userNamePositionInList][2]: #Checks if the given password matches the password that is assigned to the usermname
            print("User {}, succesfully logged on\n".format(userNameList[userNamePositionInList][1]))
            loginCheck = False

        elif userNamePositionInList == -1:        
            print("ERROR: Username incorrect!\n")

        elif userNamePositionInList != -1 and loginCheck == True:
            print("ERROR: Username Correct, Password incorrect!\n")
            userNamePositionInList = -1

    return userNameList[userNamePositionInList]

#The check user function takes a username and a ".txt" as inputs
#the function checks if the username that is passed to it exists in the given ".txt" file
#if the user exists the function returns true and if the user does not exist the function returns false.
def checkUser(userNameToCheck, userFileName):

    userNameList = getList(userFileName)
    userNameDuplicateCounter = 0
    userExistsCheck = False
    userPosition = -1

    for i in range(len(userNameList)):

        if userNameToCheck != userNameList[i][1]: 
            userNameDuplicateCounter += 1

        if userNameToCheck == userNameList[i][1]:   
            userExistsCheck = True
            userPosition = i

        if userNameDuplicateCounter == len(userNameList):
            userExistsCheck = False


    return (userExistsCheck, userPosition)

#The register user function takes a ".txt" file as the input
#It asks the user for a new username and password. 
#If the username and password fulfill certain criteria the new user details are written to the ".txt" file
def registerUser(userFileName):

    userList = getList(userFileName)
    userCheck = True
    passwordCheck = True
    newUserName = ""
    newPassword1 = ""
    newPassword2 = ""
    newUserList = []

    while userCheck:

        newUserName = input("Please enter the name of the new user: ")

        userCheck = checkUser(newUserName,userFileName)[0]

        if userCheck:
            print("ERROR: Username already exists, please enter a new username \n")


    while passwordCheck:

        newPassword1 = input("Please enter a password: ")
        newPassword2 = input("Please enter the password again: ")

        if newPassword1 == newPassword2:

            newUserList.append(len(userList)+1)
            newUserList.append(newUserName)
            newUserList.append(newPassword2)

            userList.append(newUserList)
            writeToFile(userList,userFileName)

            print("New user accepted \n{}".format(newUserList[1]))

            passwordCheck = False
        else:
            print("ERROR: Passwords did not match try again.")

    return(newUserName,newPassword1)

#The register admin function registers a new user as an admin by writing their username and password to an admin ".txt" file
def registerAdmin(userFileName,adminFileName):

    userList = getList(userFileName)
    adminList = getList(adminFileName)
    newAdmin = ""
    userExistsCheck = False
    userPosition = 0

    while userExistsCheck == False:

        newAdmin = input("Enter the username of the user you want to make an admin: ")

        userExistsCheck, userPosition = checkUser(newAdmin,userFileName)

        if userExistsCheck == False:

            print("ERROR: User not found in system please try again \n")

    adminExistsCheck = checkUser(newAdmin,adminFileName)[1]
    
    if adminExistsCheck:
        print("User {} is already an admin! \n".format(newAdmin))
    else:
        adminList.append(userList[userPosition])
        writeToFile(adminList,adminFileName)
        print("User {} is now an admin! \n".format(userList[userPosition][0]))

#the removeUser function removes a selected user from the user file and if they are an admin they are removed as an admin as well
#any tasks that were assigned to the deleted user are then automatically assigned to the user that deleted them
def removeUser(userName, userFileName, taskFileName, adminFileName):

    
    userList = getList(userFileName)
    taskList = getList(taskFileName)
    adminList = getList(adminFileName)
    userCheck = False
    userToDelete = ""
    userListPosition = -1

    while userCheck == False:

        print("*NOTE* all of their tasks will be assigned to you")
        userToDelete = input("Please enter the name of the user you want to delete: \n")

        if userName == userToDelete:
            print("ERROR: You cannot delete yourself \n")
            continue

        userCheck, userListPosition = checkUser(userToDelete,userFileName)

        if userCheck == False:
            print("ERROR: User does not exist \n")


    adminCheck, adminListPosition = checkUser(userToDelete, adminFile)

    if adminCheck:
        del adminList[adminListPosition]
    
    del userList[userListPosition]
    
    userTaskList = getUserTasks(userToDelete, taskFileName)

    for tasks in taskList:
         
         for userTasks in userTaskList:

             if tasks[0] == userTasks[0] and adminCheck == False:
                tasks[1] = userName

    writeToFile(userList, userFileName)
    writeToFile(taskList, taskFile)
    writeToFile(adminList, adminFileName)    

    print("\nUser {} deleted and {} tasks have been assigned to you".format(userToDelete, len(userTaskList)))

#the remove admin function removes a selected user from the admin list essentially revoking their admin rights
def removeAdmin(userName, adminFileName):
    
    adminList = getList(adminFileName)
    adminCheck = False
    adminToDelete = ""
    adminListPosition = -1

    while adminCheck == False:

        adminToDelete = input("Please enter the name of the user you want to remove as admin: \n")

        if userName == adminToDelete:
            print("ERROR: You cannot remove yourself as admin \n")
            continue

        adminCheck, adminListPosition = checkUser(adminToDelete,adminFileName)

        if adminCheck == False:
            print("ERROR: User does not exist or is not an admin \n")
    
    del adminList[adminListPosition]

    writeToFile(adminList, adminFileName)    

    print("\nUser {} admin rights removed".format(adminToDelete))

#The addtask function prompts the user for multiple inputs and writes those inputs as a new task to the ".txt" task file that is passed to it
def addTask(userFileName,taskFileName):

    userNameList = getList(userFileName)
    taskList = getList(taskFileName)
    userNameExistsCheck = True
    task = []
    userNameDuplicateCounter = 0

    while userNameExistsCheck:

        responsibleUser = input("Please enter the responsible user: ")

        for i in range(len(userNameList)):
            if responsibleUser != userNameList[i][1]:
                userNameDuplicateCounter += 1
            if responsibleUser == userNameList[i][1]:
                userNameExistsCheck = False
                print("User found")
            if userNameDuplicateCounter == len(userNameList):
                print("Error: user not found. Enter username again \n")
                userNameDuplicateCounter = 0

    task.append(str((len(taskList)+1)))                                                                             #task[0] = unique task number
    task.append(responsibleUser)                                                                                    #task[1] = responsible user             
    task.append(input("Please enter the title of the task: "))                                                      #task[2] = title 
    task.append(input("Please enter a description for the task: "))                                                 #task[3] = description
    task.append(date.today().strftime("%d %b %Y"))                                                                  #task[4] = assigned date 
    task.append(input("Please enter the due date of the task (day month abbreviated year eg. 25 Dec 2020): "))      #task[5] = due date 
    task.append("No")                                                                                               #task[6] = completion status 

    taskList.append(task)

    writeToFile(taskList,taskFileName)     

    print("Task succesfully Added!\n")       

#The display tasks function displays all the tasks ion the list that are passed to it in a manner which is easy to read
def displayTasks(taskList):

    if len(taskList) == 0:
        print("No tasks to display\n")
        return

    for i in range(0,len(taskList)):

        print("-----TASK {}-----".format(taskList[i][0]))
        print("RESPONSIBLE USER:\t{}".format(taskList[i][1]))
        print("TASK TITLE:\t\t{}".format(taskList[i][2]))
        print("DESCRIPTION:\t\t{}".format(taskList[i][3]))
        print("TASK ASSIGNED:\t\t{}".format(taskList[i][4]))
        print("TASK DUE:\t\t{}".format(taskList[i][5]))
        print("TASK COMPLETED:\t\t{}\n".format(taskList[i][6]))

#The get task details function takes a list of tasks as the input. 
#The function gathers data from all of these tasks and returns the data values which are used in calculations to put togethor a task report
def getTaskDetails(taskList):

    yesCounter = 0
    noCounter = 0
    overdueCounter = 0

    totalNumberOfTasks = len(taskList)

    for tasks in taskList:            
        if tasks[6] == "Yes":
            yesCounter += 1
        else:
            noCounter += 1
        if datetime.strptime(tasks[5].strip(), "%d %b %Y").date() <  date.today() and tasks[6] == "No":
                overdueCounter += 1

    return(yesCounter, noCounter, overdueCounter, totalNumberOfTasks)

#the getUserReportList takes a ".txt" user file and a task file as inputs
#the function then reads the data from these files and compiles it into information which in a list which is returned
def getUserReportList(userFileName, taskFileName):

    userList = getList(userFileName)
    taskList = getList(taskFileName)
    userTaskList = []
    userReportOneDimensional = []
    userReportTwoDimensional = []

    for user in userList:

        userReportOneDimensional = []
        userTaskList = getUserTasks(user[1], taskFileName)
        userTotalTasks = len(userTaskList)

        if  userTotalTasks == 0:
            
            percentageTasksAssigned = 0
            percentageTasksCompleted = 0
            percentageTasksToBeCompleted = 0
            percentageTasksOverdue = 0

        else:
            yesCounter, noCounter, overdueCounter, userTotalTasks = getTaskDetails(userTaskList)

            percentageTasksAssigned = round((userTotalTasks/len(taskList))*100,2)
            percentageTasksCompleted = round((yesCounter/(yesCounter+noCounter))*100,2)
            percentageTasksToBeCompleted = round((noCounter/(yesCounter+noCounter))*100,2)
            percentageTasksOverdue = round((overdueCounter/userTotalTasks)*100,2)

        userReportOneDimensional.append(user[1])                        #userReportOneDimensional[0] = user name
        userReportOneDimensional.append(userTotalTasks)                 #userReportOneDimensional[1] = users total tasks
        userReportOneDimensional.append(percentageTasksAssigned)        #userReportOneDimensional[2] = percentage of the total tasks that is assigned
        userReportOneDimensional.append(percentageTasksCompleted)       #userReportOneDimensional[3] = percentage of users tasks that are completed    
        userReportOneDimensional.append(percentageTasksToBeCompleted)   #userReportOneDimensional[4] = percentage of the users tasks that need to be completed      
        userReportOneDimensional.append(percentageTasksOverdue)         #userReportOneDimensional[5] = percentage of the users tasks that are overdue

        userReportTwoDimensional.append(userReportOneDimensional)

    return userReportTwoDimensional

#the getTaskReportList takes a ".txt" task file as an input
#the function then reads the data from the task file and compiles it into information which in a list which is returned
def getTaskReportList(taskFileName):
    taskList = getList(taskFileName)
    taskReportList = []

    yesCounter, noCounter, overdueCounter, totalNumberOfTasks = getTaskDetails(taskList)

    if taskList == 0:
        percentageTasksIncomplete = 0
        percentageOverdue = 0
    else:
        percentageTasksIncomplete = round((noCounter/totalNumberOfTasks)*100,2)
        percentageOverdue = round((overdueCounter/totalNumberOfTasks)*100,2)

    taskReportList.append(totalNumberOfTasks)
    taskReportList.append(yesCounter)
    taskReportList.append(overdueCounter)
    taskReportList.append(noCounter)
    taskReportList.append(percentageTasksIncomplete)
    taskReportList.append(percentageOverdue)

    return taskReportList

#The writeUserReport function takes three ".txt" files as inputs. It performs minor calculations on the data in these files
#and then writes the information to the ".txt" report file that is passed to it in a manner which is easy for the user to read
def writeUserReport(userFileName, taskFileName, userReportFileName, adminFileName):
    userList = getList(userFileName)
    taskList = getList(taskFileName)
    adminList = getList(adminFileName)
    userReportList = getUserReportList(userFileName, taskFileName)

    with open(userReportFileName,"w") as f:

        f.write("\nThe total number of registered users is:\t\t{}\n".format(len(userList)))
        f.write("The total number of admins is:\t\t\t\t\t{}\n".format(len(adminList)))
        f.write("The total number of registered tasks is:\t\t{}\n\n".format(len(taskList)))

        for users in userReportList:
            f.write("_____User: {}_____ \n".format(users[0]))
            f.write("Total tasks assigned to user:\t\t\t\t\t{} \n".format(users[1]))
            f.write("Percentage of all tasks owned:\t\t\t\t\t{}% \n".format(users[2]))
            f.write("Percentage of assigned tasks completed:\t\t\t{}% \n".format(users[3]))
            f.write("Percentage of assigned tasks to be completed:\t{}% \n".format(users[4]))
            f.write("Percentage of assigned tasks overdue:\t\t\t{}% \n".format(users[5]))

    f.close()

#The printFile function prints the file that is passed to it
def printFile(fileName):

    File = open(fileName,"r")

    for line in File:
        print(line.replace("\t", ""))

    print("\n")

#The writeTaskReport function takes two ".txt" files as inputs. It performs minor calculations on the data in these files 
#and then writes the information to the ".txt" report file that is passed to it in a manner which is easy for the user to read
def writeTaskReport( taskFileName, taskReportFileName):

    taskReportList = getTaskReportList(taskFileName)

    with open(taskReportFileName,"w") as f:

        f.write("_____ALL TASK DETAILS_____".format(taskReportList[0]))
        f.write("\nRegistered tasks:\t\t\t\t{} \n".format(taskReportList[0]))
        f.write("Completed tasks:\t\t\t\t{} \n".format(taskReportList[1]))
        f.write("Uncompleted tasks:\t\t\t\t{} \n".format(taskReportList[2]))
        f.write("Tasks that are overdue:\t\t\t{} \n".format(taskReportList[3]))
        f.write("Percentage tasks incomplete:\t{}% \n".format(taskReportList[4]))
        f.write("Percentage overdue:\t\t\t\t{}% \n".format(taskReportList[5]))

    f.close()

#the viewMyTasksMenu acts as a sub menu to the main menu. It allows the user to view their tasks as well as edit certain aspects of the tasks that are assigned to them
def viewMyTasksMenu(userName,userFileName,taskFileName):

    taskList = getList(taskFileName)
    userTaskList = getUserTasks(userName,taskFileName)
    taskNumberCheck = True
    userSelectionCheck = True
    userResponsibleCheck = False
    userTaskCheck = True
    userSelection = ""
    taskNumber = 0

    if len(userTaskList) == 0:                  #Checks if the user has tasks to edit
        print("You have no tasks to edit \n")
        return

    while userSelectionCheck:
        userTaskList = getUserTasks(userName,taskFileName)
        displayTasks(userTaskList)

        while taskNumberCheck:

            taskNumber = int(input("Enter the number of the task you want to edit or enter -1 to exit to the main menu: "))

            if taskNumber == -1:
                return

            if taskNumber > len(taskList) or taskNumber < 0:    #Checks that the task number is an existing task number
                print("ERROR: Must enter an existing task number \n")
                continue

            elif taskList[taskNumber][6] == "Yes":              #checks that the task has not been completed already
                print("ERROR: Can only edit tasks with a completion status of 'No'\n")
                continue

            for userTasks in userTaskList:  #checks that the task number is assigned to the user

                if userTasks[0] == taskNumber:
                    taskNumberCheck = False
                    userTaskCheck = False
                    continue

            if userTaskCheck:
                print("ERROR: Must enter task number that is assigned to you \n")

        userSelection = input("Please select one of the following options: \n m - mark a task as complete \n r - edit a tasks responsible user \n d - edit a task due date \n e - exit to the main menu \n")

        #If user selection = "e"
        if userSelection == "e":
            userSelectionCheck = False
            continue
        
        #If user selection = "m"
        if userSelection == "m":
            taskList[taskNumber][6] = "Yes"
            writeToFile(taskList,taskFileName)
            print("Task {} succesfully marked as complete \n".format(taskNumber))

        #If user selection = "r"
        elif userSelection == "r":

            while userResponsibleCheck == False:    #Checks if the new responsible user is an existing user

                newUser = input("Enter the name of the new responsible user: ")

                userResponsibleCheck= checkUser(newUser,userFileName)[0]

                if userResponsibleCheck == False:
                    print("ERROR: Username does not exist, please enter a new username. \n")

            taskList[taskNumber][1] = newUser
            writeToFile(taskList,taskFileName)

            print("{} is now assigned as the responsible user of task {} \n".format(newUser, taskNumber))

        #If user selection = "d"
        elif userSelection == "d":

            newDate = input("Please enter a new due date for the task: ")
            taskList[taskNumber][5] = newDate
            writeToFile(taskList,taskFileName)

            print("The due date of task {} has been updated to {} \n".format(taskNumber, newDate))

        else:
            print("ERROR: input not recognized please try again.")

        taskNumberCheck = True

#The mainMenu function takes a user input and depending on the users input it calls the appropriate functions
#the menu also checks if the user is an admin or not and displays the appropriate main menu
def mainMenu(userName,userFileName,taskFileName, adminFileName, userReportFileName, taskReportFileName):

    menuCheck = True
    loggedInCheck = True
    adminCheck = False
    userSelection = ""

    adminCheck = checkUser(userName,adminFileName)[0]

    while menuCheck:

        if adminCheck:  #checks if user is admin
            print("Please select one of the following options: \n r  - register user \n du - display users and passwords \n ru - remove user \n ra - register admin \n\
 da - display all admins and passwords \n fa - remove user admin rights \n a  - add task \n va - view all tasks \n\
 vm - view my tasks/edit my tasks \n ds - display statistics from current reports \n gr - generate new reports \n l  - logoff \n e  - exit program and log off \n")
            userSelection = input()
        else:       #else case for when the user is not an admin
            print("Please select one of the following options: \n a  - add task \n va - view all tasks \n vm - view my tasks/edit my tasks \n l  - logoff \
                \n e  - exit program and log off \n")
            userSelection = input()

        if userSelection == "r" and adminCheck: 
            registerUser(userFileName)

        elif userSelection == "du" and adminCheck:
            printFile(userFile)

        elif userSelection == "ru" and adminCheck:
            removeUser(userName, userFileName, taskFileName, adminFileName)
            
        elif userSelection == "ra" and adminCheck:
            registerAdmin(userFileName,adminFileName)

        elif userSelection == "da" and adminCheck:
            printFile(adminFile)

        elif userSelection == "fa" and adminCheck:
            removeAdmin(userName, adminFileName)   

        elif userSelection == "a":
            addTask(userFileName,taskFileName)

        elif userSelection == "va":
            displayTasks(getList(taskFileName))

        elif userSelection == "vm":
            viewMyTasksMenu(userName,userFileName,taskFileName)

        elif userSelection == "ds" and adminCheck:  
            printFile(userReportFileName)
            printFile(taskReportFileName)

        elif userSelection == "gr" and adminCheck:  
            writeUserReport(userFileName, taskFileName, userReportFileName, adminFileName)
            writeTaskReport( taskFileName, taskReportFileName)

        elif userSelection == "l":
            print("Succesfully Logged Off user: {}\n".format(userName))
            menuCheck = False

        elif userSelection == "e":
            print("Succesfully Logged Off user: {}\n".format(userName))
            print("Program exited, have a nice day!")
            menuCheck = False
            loggedInCheck = False

        else:
            print("ERROR: input not accepted please try again \n")

    return loggedInCheck

#__________This is the main body of the code__________#

user = [] #defines an empty list used to store the logged in users username and password

#defines the files that will be read and written to by the functions
userFile = "user.txt"
taskFile = "tasks.txt"
adminFile = "admin.txt"
userReportFile = "user_overview.txt"
taskReportFile = "task_overview.txt"

#variables used to check if a user is still logged in
loggedInCheck = True

#loops the main menu until the user exits
while loggedInCheck:

    user = login(userFile)
    loggedInCheck = mainMenu(user[1],userFile,taskFile, adminFile, userReportFile, taskReportFile)

