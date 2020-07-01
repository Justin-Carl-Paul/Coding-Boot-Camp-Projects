#imports the math library
import math

#Prints a menu which explains to the user how to use the application 
print("Choose either 'investment or 'bond' from the menu below to proceed: \n")
print("investment       - to calculate the amount of interest you'll earn on interest")
print("bond             - to calculate the amount you'll have to pay on a home loan")

userSelection = str.lower(input())                                          #accepts the users input as a string. The string is then set to all lower case. Therefore the users input is not case sensetive

if userSelection == "investment":                                           #checks if 'userSelection' is equal to the string "investment"
    #If 'userSelection' is equal to "investment" then the user is asked to enter the details of their investment. The users inputs are cast from a string to a floating point
    deposit = float(input("How much money are you depositing?: "))
    interestRate = (float(input("What is the interest rate of the investment?: "))/100)
    years = float(input("For how many years is your investment?: "))
    interest = str.lower(input("Is the investment 'simple' or 'compound' interest?: "))

    if interest == "simple":                                                #checks if 'interest' is equal to the string "simple"
        total = round(deposit*(1+interestRate*years),2)                     #calculates the total of the investment using the simple interest rate formula
        print("The total amount of your investment is R {}".format(total))  #prints the total value of the investment for the user to see
    
    elif interest == "compound":                                            #else if case which checks if 'interest' is equal to the string "compound"
        total = round(deposit*math.pow((1+interestRate),years),2)           #calculates the total of the investment using the compound interest rate formula
        print("The total amount of your investment is R {}".format(total))  #prints the total value of the investment for the user to see

    else:                                                                   #else case which is entered if the user did not enter in either "simple" or "compund" into the 'interest' input prompt
        print("ERROR - input not recognized: Please ensure that you enter in either 'simple' or 'compound'")    #displays an error message to the user informing them that their input was incorrect

elif userSelection == "bond":                                               #checks if 'userSelection' is equal to the string "bond"
    #If 'userSelection' is equal to "bond" then the user is asked to enter the details of their bond. The users inputs are cast from a string to a floating point
    houseValue = float(input("What is the value of the house?: "))
    interestRate = (float(input("What is the annual interest rate of the bond?: ")))/12 #calculates the monthly interest rate by dividing the annual interest rate by 12
    months = float(input("What is the duration of the bond in months?: "))

    rePayment = round((interestRate*houseValue)/(1-(1+interestRate)**(-months)),2)  #determines the monthly re-payment rate of the bond and rounds it off to the second decimal place
    print("The monthly re-payment on your bond is R {}".format(rePayment))          #prints the monthly repayment for the user to see

else:                                                                       #else case which is entered if the user did not enter in either "bond" or "investment" into the 'userSelection' input prompt
    print("ERROR - input not recognized: Please ensure that you enter in either 'investment' or 'bond'")        #displays an error message to the user informing them that their input was incorrect