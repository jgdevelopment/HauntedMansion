import java.lang.Math;
import java.util.ArrayList;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * Adam wrote the initializer and began work on the win condition.
 * Jason began to write the Item Class
 *
 * 
 * @author  Adam Shaw and Jason Ginsberg
 * @version 2014.10.1
 */

public class Game 
{
    //initializations by Jason Ginsberg
    private Parser parser;
    public static Room currentRoom;
    public static User user;
    private ArrayList<Room> roomsVisited;
    private String direction;

    private Character ogre, wizard;
    private String characterDescription;

    private boolean wantToQuit;

    private Items map,
    key,
    food,
    closet,
    medicine,
    hint;

    private boolean usedKey;

    //room initializations by Adam Shaw
    private Room masterBedroom,
    study,
    livingRoom,
    entranceHall, 
    outside,
    library, 
    diningRoom,
    wineCellar, 
    dungeon,
    kitchen,
    nextRoom,
    drawingRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        user = new User();
        //revised quit function by Adam
        wantToQuit=false;
    }

    /**
     * Create all the rooms, their characters, items, and link their exits together.
     */
    private void createRooms()
    {
        //create the charatcers by Adam
        ogre = new Character(
            "ogre",
            "What's up",
            "The sky");
        wizard = new Character(
            "wizard",
            "What is the Answer to the ultimate question of life, the universe, and everything",
            "42");

        // create the items by Jason
        map = new Items("map");
        map.setWeight(25); // inventory max capacity is 100
        Command readMap = new Command("read", "map");
        map.setPermissions(readMap);

        key = new Items("key");
        key.setWeight(5); 
        Command use = new Command("use", "food");
        key.setPermissions(use);

        food = new Items("food");
        food.setWeight(10); 
        Command eat = new Command("eat", "food");
        food.setPermissions(eat);

        medicine = new Items("medicine");
        medicine.setWeight(10); 
        Command drink = new Command("drink", "medicine");
        medicine.setPermissions(drink);

        hint = new Items("clue");
        hint.setWeight(5); 
        Command readHint = new Command("read", "clue");
        hint.setPermissions(readHint);

        // create the rooms by Adam
        masterBedroom = new Room("in the master bedroom");
        study = new Room("in the study");
        livingRoom = new Room("in the living room");
        entranceHall = new Room("in the entrace hall of the Mansion");
        outside  = new Room("outside of the Mansion! You win!");
        library = new Room("in the library");
        diningRoom = new Room("in the dining room");
        wineCellar = new Room("in the wine cellar");
        dungeon = new Room("in the mansion's dungeon");
        kitchen = new Room("in the kitchen");
        drawingRoom = new Room("in the drawing room");

        // initialise rooms and their exits by Adam
        //items and characters initialized in rooms by Jason
        masterBedroom.setExit("east", study);
        masterBedroom.setItem(key);

        study.setExit("west", masterBedroom);
        study.setExit("south", library);        
        study.setExit("east", livingRoom);
        study.setItem(map);

        livingRoom.setExit("west", study);
        livingRoom.setExit("south", diningRoom);
        livingRoom.setExit("east", entranceHall);
        livingRoom.setCharacter(wizard);

        entranceHall.setExit("west", livingRoom);
        entranceHall.setExit("north", outside);
        entranceHall.setIsLocked(true, "north");
        usedKey = false; // set the entrance door to unlockable without key

        outside.setExit("south", entranceHall);

        library.setExit("north", study);
        library.setExit("east", diningRoom);
        library.setItem(medicine);

        diningRoom.setExit("north", livingRoom);
        diningRoom.setExit("west", library);
        diningRoom.setExit("south", kitchen);

        kitchen.setExit("north", diningRoom);
        kitchen.setExit("east", drawingRoom);
        kitchen.setExit("south", wineCellar); 
        kitchen.setItem(food);

        drawingRoom.setExit("west",kitchen);
        drawingRoom.setCharacter(ogre);

        dungeon.setExit("east",wineCellar);
        dungeon.setItem(hint);
        dungeon.setIsLocked(true, "east");

        wineCellar.setExit("north",kitchen);
        wineCellar.setExit("west",dungeon);

        currentRoom = dungeon;  // start game in dungeon
        roomsVisited = new ArrayList<Room>();
        roomsVisited.add(dungeon);
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over
        boolean finished = false;
        while (!finished) 
        {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() //written by Jason
    {
        System.out.println("************************************************");
        System.out.println();
        System.out.println("Welcome to the Haunted Mansion!");
        System.out.println("You wake up with a headache and a sharp pain in your arm.");
        System.out.println("You stand up but when you try to walk you suddenly trip");
        System.out.println("and realize you're shackled to the floor.");
        System.out.println("You're stuck in a dungeon. The room is dark.");
        System.out.println();
        System.out.println("Try to escape.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
        System.out.println("Type 'help' if you need help.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) //implemented by Jason
    {
        String commandWord = command.getCommandWord();
        String object = command.getSecondWord();
        Items item = parser.getCommandItem(); //item being acted on by command change from parser to items, add ca
        if(command.isUnknown()) //first check command exists
        {
            printUnknownCommand();
            return false;
        }
        if (commandWord.equals("go")) //if command is "acting" on a direction
        {
            proceed(command);
        }
        else if  (commandWord.equals("talk")) // if the command is acting on an a character        
        {
            Character character = currentRoom.getCharacterFromString(object);
            if (character!=null){
                talk(character);
            }
            else{
                printCharacterWarning();
                return false;
            }
        }
        else if (item==null) // if the command is not acting on an item
        {
            if (commandWord.equals("help")) 
            {
                printHelp();
            }
            else if (commandWord.equals("quit")) 
            {
                wantToQuit = quit(command);
            }
            else
            {
                printInvalidObject();
                return false;
            }
        }
        // if the command is acting on an a item, first check that command can be used on object then call method for command
        else if (item.isValidCommand(commandWord)){
            if (commandWord.equals("add")) 
            {
                pickUp(item);
            }
            else if (commandWord.equals("eat")) 
            {
                eat();
            }
            else if (commandWord.equals("read")) 
            {
                if (command.getSecondWord().equals("map"))
                { 
                    printMap();
                }
                else if (command.getSecondWord().equals("clue"))
                {
                    printClue();
                }
            }
            else if (commandWord.equals("use")) 
            {
                use();
            }
            else if (commandWord.equals("drop")) 
            {
                drop(item);
            }
            else if (commandWord.equals("drink")) 
            {
                drink();
            }
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * prints the ASCII code map
     */
    private void printMap()  //map by Adam
    {
        System.out.println("____________________________________________________________|exit|___");
        System.out.println("|                |                |                |                |");
        System.out.println("|     master     |     study      |     living     |    entrance    |");
        System.out.println("|    bedroom     |                |      room      |      hall      |");
        System.out.println("|________________|________________|________________|________________|");
        System.out.println("                 |                |                |");
        System.out.println("                 |    library     |     dining     |");
        System.out.println("                 |                |      room      |");
        System.out.println("                 |________________|________________|_________________");
        System.out.println("                                  |                |                |");
        System.out.println("                                  |     kitchen    |     drawing    |");
        System.out.println("                                  |                |      room      |");
        System.out.println("                  ________________|________________|________________|");
        System.out.println("                 |                |                |");
        System.out.println("                 |    dungeon     |      wine      |");
        System.out.println("                 |                |     cellar     |");
        System.out.println("                 |________________|________________|");
    }

    private void printClue()
    {
        System.out.println("the ocean blue");
    }

    private void proceed(Command command)
    {
        direction = command.getSecondWord();
        nextRoom = currentRoom.getExit(direction);
        if (checkNextRoom(command,nextRoom)) //checks if direction is valid
        {
            if(checkLockedDoor()) // if door is unlocked, then user can proceed to next room
            {
                goRoom(command);
            }
        }
    }

    private void printCharacterWarning()
    {
        System.out.println("that is not a character");
    }

    private void printUnknownCommand()
    {
        System.out.println("I don't know what you mean...");
    }

    private void printMedicineWarning()
    {
        System.out.println("Before drinking the medicine you realize that is probably not the best idea and close the lid.");
        System.out.println("Maybe it will be useful later.");
    }

    private void printInvalidObject()
    {
        System.out.println("That is not an object."); 
    }

    private void drink()
    {
        if (user.getSickCondition())
        {
            user.makeWell();
        }
        else
        {
            printMedicineWarning();
        }
    }

    /**
     * pickUp(Items item):
     * attempts to pick up an item and add it to the user's inventory.
     * if it is to heavy, the system prints a line telling the user the object cannot
     * be picked up
     */
    private void pickUp(Items item)
    {
        if (user.isInventoryFull())
        {
            System.out.println("Cannot pick up item. Inventory is full");
        }
        else{
            //add item to user weight and inventory for other commands like open,
            user.addItem(item);            
            System.out.println("Current Inventory Weight: "+user.getWeight());
        }
    }

    private void talk(Character character)
    {
        System.out.println("You approach the " + character.getDescription() + " and ask him '" + character.getQuestion()+"?'");
        System.out.println("The " + character.getDescription() + " replies: "+character.getResponse());
        if (character == wizard){
            user.makeSick();
            System.out.println("The wizard cursed you with a sickness");
        }
    }

    private void use()
    {
        if (currentRoom==entranceHall)
        {
            usedKey = true;
            System.out.println("You insert the key. Now try to open the door.");
        }
        else
        {
            System.out.println("Go to the entrance hall.");
        }
    }

    /**
     * drop(Items item)
     * drops an item from the user's inventory, using a class inherited form the User Class.
     * this eliminates the item from the ArrayList<Items> inventory and removes that weight from 
     * the amount the user is currently carrying, and updates the inventory String to
     * reflect what the user is carrying
     */
    private void drop(Items item)
    {
        System.out.println(item.getDescription()+" removed from inventory");
        user.removeItem(item);
    }

    private void eat()
    {
        System.out.println("You eat the food, but quickly feel sick. It seems that it was poisoned");
        user.makeSick();
        user.addWeight(food.getWeight());
        System.out.println("If you do not find the antidote soon you will die!"); 
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println();
        System.out.println("You are lost. You are scared. You wander");
        System.out.println("around the haunted mansion.");
        System.out.println();
        System.out.println("You are not alone.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    private boolean checkLockedDoor()
    {
        if (currentRoom.isLocked() && direction.equals(currentRoom.getLockedDirection()))
        {
            if (currentRoom == dungeon)
            {
                System.out.println("You try to open the door but find that it is locked!");
                System.out.println("You notice a 4 digit PIN number pad next to the door.");
                System.out.println("Enter Passcode: ");
                int code= parser.getInt();
                if (code == 1492)
                { //answer to riddle
                    System.out.println("You have unlocked the door. You proceed into the next room.");
                    currentRoom.setIsLocked(false, "east");
                    return true;
                }
                else
                {
                    System.out.println("Incorrect code.");
                    if (code==0){
                        System.out.print("passcode must be a 4-digit PIN");
                        System.out.println();
                    }
                    return false;
                }
            }
            else if (currentRoom == entranceHall)
            {
                if (usedKey)
                { //answer to riddle
                    System.out.println("You have unlocked the door. You escape outside.");
                    currentRoom.setIsLocked(false, "north");
                    return true;
                }
                else
                {
                    System.out.println("Cannot open door. It seems to be locked.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkNextRoom(Command command, Room nextRoom)
    {
        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
            return false;
        }
        return true;
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        roomsVisited.add(currentRoom);
        nextRoom.setExit("back",roomsVisited.get(roomsVisited.size()-1));
        currentRoom = nextRoom;
        printStats();
        if (currentRoom==outside)
        {
            endGame("win");
        }
        updateHealth();
    }

    private void printStats()
    {
        System.out.println("************************************************");
        System.out.println(currentRoom.getLongDescription());
        System.out.println("Current Inventory Items: "+user.getInventoryItems());
        System.out.println("Characters in room: "+currentRoom.getCharacterDescription());
    }

    private void updateHealth()
    {
        if(Math.random()<(0.05)) //randomly get sick
        {
            user.makeSick();
            System.out.println("You've been struck with a sudden sickness!");
        }
        if(user.getSickCondition())
        {
            System.out.println("You have " +  user.getTimeLeft() + " turns left to find medicine before you die!");
            user.lostTime();
            if(user.getTimeLeft() <0)
            {
                System.out.println("You have died of sickness!");
                endGame("lose");
            }
        }
    }

    private void endGame(String condition)
    {
        if (condition.equals("win"))
        {
            System.out.println("You win!");
            wantToQuit=true;
        }
        else
        {
            System.out.println("You lose!");
            wantToQuit=true;
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) 
        {
            System.out.println("Quit what?");
            return false;
        }
        else 
        {
            return true;  // signal that we want to quit
        }
    }
}
