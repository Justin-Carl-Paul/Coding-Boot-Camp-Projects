#imports the necessary libraries
import pygame 
import random
from pygame import mixer

#initializes pygame
pygame.init()

#Background Sound which is played and looped repeatedly for the duration of the game
mixer.music.load("background.mp3")
mixer.music.play(-1)

#loads the background image
background = pygame.image.load("background.png")

#Creates a screen which is 1500 pixels wide by 1000 pixels high
screen_width = 1500
screen_height = 1000
screen = pygame.display.set_mode((screen_width,screen_height))

#Changes the Title and the icon of the created screen
pygame.display.set_caption("Gerald: The Ultimate Hunter of Beasts")
icon = pygame.image.load("warrior.png")
pygame.display.set_icon(icon)

#Loads the font of the text and sets the x and y positions for the where the text is displayed on the screen
font = pygame.font.Font("text.ttf",40)
textX = 0
textY = 0

#Message Function: renders and then blits the message on the screen at the specified x and y co-ordinates
def message(x,y):
    message = font.render("Avoid the enemies and obtain the jewel to WIN!",True,(255,255,0))
    screen.blit(message,(x,y))

#Player data such as the image which is used and the player starting x and y positons
playerImg = pygame.image.load("wizard.png")
player_height = playerImg.get_height()
player_width = playerImg.get_width()
playerX = 10
playerY = screen_height/2
playerMovementSpeed = 8

#Enemy data such as the image which is used and the enemy starting x and y positons. Lists are used so that more than one enemy can be spawned at a time.
#The information of all of the enemies will be placed in each list
enemyImg = []
enemyX = []
enemyY = []
enemyMovementSpeed = []
numOfEnemies = 3

#Popultes the above lists with the desired number of enemies. This allows you to have multiple enimies on screen.
for i in range(numOfEnemies):
    enemyImg.append(pygame.image.load("troll{}.png".format(i)))
    enemy_height = enemyImg[i].get_height()
    enemy_width = enemyImg[i].get_width()
    enemyX.append(screen_width)
    enemyY.append(random.randint(0,screen_height - enemy_height))
    enemyMovementSpeed.append(random.randint(4,7))  #Each enemy has a random movement speed to keep the game fun and exciting

#Reward Data such as which image should be used as well as the starting X and Y positions 
rewardImg = pygame.image.load("jewel.png")
reward_height = rewardImg.get_height()
reward_width = rewardImg.get_width()
rewardX = 1300
rewardY = 750
rewardMovementSpeed = 5
rewardYChange = 8

#Player method which is placed inside the game loop. The method is passed the image as well as the x and y positions of where the image shopuld be blitted.
def player(image,x,y):
    screen.blit(image,(x,y))

#Enemy method which is placed inside the game loop. The method is passed the image as well as the x and y positions of the image.
def enemy(image,x,y):
    screen.blit(image,(x,y))

#enemy movement method which can be called to spawn different enemies. The method is passed the number of the enemy that you are spawning
def enemyMovement(i):
        if playerY < enemyY[i]: #If statement which moves the enemy towards the player in the Y direction. If the player is 'lower' than the enemy the enemy moves 'lower' as well
            enemyY[i] -= enemyMovementSpeed[i]/2
        if playerY > enemyY[i]: #If statement which moves the enemy towards the player in the Y direction. If the player is 'higher' than the enemy the enemy moves 'higher' as well
            enemyY[i] += enemyMovementSpeed[i]/2
        enemyX[i] -= enemyMovementSpeed[i]  #Each enemy moves at a constant rate in the x direction
        #Defines the Enemy Box
        enemyBox = pygame.Rect(enemyImg[i].get_rect())
        enemyBox.top = enemyY[i]
        enemyBox.left = enemyX[i]
        enemyBox.bottom = enemyY[i]+enemy_height
        enemyBox.right = enemyX[i]+enemy_width
        enemy(enemyImg[i],enemyX[i],enemyY[i])
        #An if statement which checks if the enemy box has collided with the player box
        #If the enemy box collides with the player box then the game window closes and the player looses the game
        if playerBox.colliderect(enemyBox):
            print("You loose!")
            pygame.quit()
            exit(0)

#Reward Method which is placed inside the game loop. The method is passed the image as well as the x and y positions of the image.
def reward(x,y):
    screen.blit(rewardImg,(x,y))

#initializes necessary variables as False
keyUp = False
keyDown = False
keyLeft = False
keyRight = False

#Start of the game loop
while True:
    #refreshes the screen with each iteration of the while loop so that the old image blits are removed and refreshed
    pygame.display.update()

    #Creates the background image by tiling one 500 x 500 pixels image in 6 different places on the screen
    screen.blit(background, (0,0))
    screen.blit(background, (0,500))
    screen.blit(background, (500,0))
    screen.blit(background, (500,500))
    screen.blit(background, (1000,0))
    screen.blit(background, (1000,500))

    #Creates the player,reward and info message images by calling the appropriate methods. The x and y positions for the player and reward methods are updated with each while iteration
    #This creates the illusion that the images are moving on the screen when actually a new image is generated in a new position with each iteration and the previous image is removed
    player(playerImg,playerX,playerY)
    reward(rewardX,rewardY)
    message(textX,textY)

    #Checks if the quit button on the screen is pushed. If the quit button is pushed the game is exited.
    for event in pygame.event.get():

        if event.type == pygame.QUIT:
            pygame.quit()
            exit(0)
    
    #Logic for the player movement
    #checks if a key is pressed then if the up, down, left or right key is pressed the appropriate variable is set to true
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_UP:
                keyUp = True
            if event.key == pygame.K_DOWN:
                keyDown = True
            if event.key == pygame.K_LEFT:
                keyLeft = True
            if event.key == pygame.K_RIGHT:
                keyRight = True

    #Checks if keys are released on the keyboard if the up, down, left or right key is pressed the appropriate variable is set to false
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_UP:
                keyUp = False
            if event.key == pygame.K_DOWN:
                keyDown = False
            if event.key == pygame.K_LEFT:
                keyLeft = False
            if event.key == pygame.K_RIGHT:
                keyRight = False
    #Depending on which variable is true the player x and y positions are incremented to move the player image up, down, left or right
    if keyUp == True:
        if playerY > 0:
            playerY -= playerMovementSpeed
    if keyDown == True:
        if playerY < screen_height-player_height:
            playerY += playerMovementSpeed
    if keyLeft == True:
        if playerX > 0:
            playerX -= playerMovementSpeed
            playerImg = pygame.image.load("wizard.png") #Changes the image used for the player sprite depending on the direction they are moving to make the movement feel more realistic
    if keyRight == True:
        if playerX < screen_width-player_width:
            playerX += playerMovementSpeed
            playerImg = pygame.image.load("wizard_left.png") #Changes the image used for the player sprite depending on the direction they are moving to make the movement feel more realistic

    #logic for reward movement. The reward moves up and down in the Y direction until it either hits the top or bottom of the screen.
    #when it hits the top or the bottom of the screen then it moves to the left in the x direction by 68 pixels
    if rewardY <= 0:
        rewardYChange = rewardMovementSpeed
        rewardX -= reward_width/2
    if rewardY >= screen_height - reward_height:
        rewardYChange = -rewardMovementSpeed
        rewardX -= reward_width/2
    rewardY += rewardYChange 

    #Defines the Player Box
    playerBox = pygame.Rect(playerImg.get_rect())
    playerBox.top = playerY
    playerBox.left = playerX
    playerBox.bottom = playerY+player_height
    playerBox.right = playerX+player_width

    #Defines the Reward Box
    rewardBox = pygame.Rect(rewardImg.get_rect())
    rewardBox.top = rewardY
    rewardBox.left = rewardX
    rewardBox.bottom = rewardY+reward_height
    rewardBox.right = rewardX+reward_width

    #calls the enemy movement method to spawn the three different enemies at different times from each other
    enemyMovement(0)
    if enemyX[0] < 1200:
        enemyMovement(1)
    if enemyX[1] < 900:
        enemyMovement(2)


    #An if statement which checks if the player box has collided with the reward box
    #If the player box collides with the enemy box then the game window closes and the player WINS the game
    if playerBox.colliderect(rewardBox):
        print("You Win!")
        pygame.quit()
        exit(0)

