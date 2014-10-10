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
 * Log (by Adam Shaw):
 * Adam wrote the room initializations.
 * Jason wrote  the processCommand method and the methods for each command
 * Jason wrote the item class
 * Adam wrote the Character class
 * Jason added a parseInt method to Parser
 * Jason wrote the room class
 * Jason wrote the sick methods
 * Jason wrote the inventory and getItem/getCommand from string
 * Adam wrote the sickRandomizer
 * Jason refactored much of the code in order to
 *     -better reflect principles of high cohesion, low coupling
 *     -In this, Jason eliminated the User class, integrating its
 *     features, such as the inventory, checkItemPermissions, 
 *     makeSick, among others, into the Game Class
 * Jason edited Item add and drop methods
 * @author  Adam Shaw and Jason Ginsberg
 * @version 2014.10.1
 */

public class Game 
{
    private Parser parser;
    private ArrayList<Item> inventory;
    private ArrayList<Room> roomsVisited;
    private boolean wantToQuit;
    private boolean isSick;
    private boolean usedKey;
    private int weight;
    private int timeLeft;
    private int INVENTORY_CAPACITY=100;
    private String direction;
    private Character ogre, wizard;
    private Room currentRoom,
    nextRoom,
    masterBedroom,
    study,
    livingRoom,
    entranceHall,
    outside,
    library,
    diningRoom,
    wineCellar,
    dungeon,
    kitchen,
    drawingRoom;

    /**
     * Create the game and initialise its internal map and configurations.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        inventory = new ArrayList<Item>();
        wantToQuit=false;
        weight = 0;
    }

    /**
     * Create all the rooms, their characters, items, and link their exits together.
     */
    private void createRooms()
    {
        ogre = new Character(
            "ogre",
            "What's an ogre's favorite song",
            "Somewhere Ogre the Rainbow");
        wizard = new Character(
            "wizard",
            "What is the Answer to the ultimate question of life, the universe, and everything",
            "42");

        //item initializations by JG
        Item map, key, food, medicine, hint;
        map = new Item(
            "map",
            25,
            true,
            "read");
        key = new Item(
            "key",
            5,
            true,
            "use");
        food = new Item(
            "food",
            10,
            true,
            "eat");
        medicine = new Item("medicine",
            10,
            true,
            "drink");
        hint = new Item(
            "clue",
            0,
            false,
            "read");

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

        masterBedroom.setExit("east", study);
        masterBedroom.addItem(key);
        study.setExit("west", masterBedroom);
        study.setExit("south", library);        
        study.setExit("east", livingRoom);
        study.addItem(map);
        livingRoom.setExit("west", study);
        livingRoom.setExit("south", diningRoom);
        livingRoom.setExit("east", entranceHall);
        livingRoom.addCharacter(wizard);
        entranceHall.setExit("west", livingRoom);
        entranceHall.setExit("north", outside);
        entranceHall.setIsLocked(true, "north");
        usedKey = false; // set the entrance door to unlockable without key
        outside.setExit("south", entranceHall);
        library.setExit("north", study);
        library.setExit("east", diningRoom);
        library.addItem(medicine);
        diningRoom.setExit("north", livingRoom);
        diningRoom.setExit("west", library);
        diningRoom.setExit("south", kitchen);
        kitchen.setExit("north", diningRoom);
        kitchen.setExit("east", drawingRoom);
        kitchen.setExit("south", wineCellar); 
        kitchen.addItem(food);
        drawingRoom.setExit("west",kitchen);
        drawingRoom.addCharacter(ogre);
        dungeon.setExit("east",wineCellar);
        dungeon.addItem(hint);
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
    private void printWelcome()            
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
        currentRoom.printItemDescription();
        System.out.println();
        System.out.println("Type 'help' if you need help.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param Command command the command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        if(command.isUnknown()) //first check command exists
        {
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("go")) 
        {
            goRoom(command);
        }
        else if (commandWord.equals("talk")) // talk to character  
        {
            talk(command);
        }
        else  if (commandWord.equals("help")) 
        {
            printHelp();
        }
        else if (commandWord.equals("quit")) 
        {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("add")) // add item
        {
            add(command);
        }
        else if (commandWord.equals("eat")) // eat food
        {
            eat(command);
        }
        else if (commandWord.equals("read")) // read clue or map
        {
            printInfo(command);
        }
        else if (commandWord.equals("use")) // use a key
        {
            use(command);
        }
        else if (commandWord.equals("drop")) // drop item
        {
            drop(command);
        }
        else if (commandWord.equals("drink")) // drink medicine
        {
            drink(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    /**
     * Given an String, determinee if that string is an available item for the user.
     * @param String itemWord the string to be processed.
     * @return Item if the string represents an item in the inventory or room, otherwise return null
     */
    private Item getItemFromString(String itemWord) 
    {
        for (Item item: currentRoom.getItems()) 
        // if I had more time I would make an itemInInventory and itemInRoom method to check this (started inInventory below)
        {
            if (item.getDescription().equals(itemWord))
            {
                return item;
            }
        }
        for (Item item: inventory) // checks if item is in inventory then in room
        {
            if (item.getDescription().equals(itemWord))
            {
                return item;
            }
        }
        System.out.println("That object is not in this room."); 
        return null;
    }

    /**
     * Given an item, determine if it is in the inventory
     * @param Item item the item to be checked for in the inventory  
     * @return true if item is in inventory and inventory exists
     */
    private boolean itemInInventory(Item item)
    {
        if (inventory!=null)
        {
            for (Item inventoryItem: inventory) // make sure the item is not already in the inventory
            {
                if (item.getDescription().equals(inventoryItem.getDescription()))
                {
                    System.out.println("Item already added to inventory");
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Given a command check that the item had permission to use command
     * @param Command command the command to be process
     * @return true if the item can use that command
     */
    private boolean checkObjectCommand(Command command) 
    {
        String itemWord = command.getSecondWord();
        String commandWord = command.getCommandWord();
        Item item = getItemFromString(itemWord);
        if (item!=null)
        {
            for (Command allowedCommand: item.getPermission())
            {
                if (allowedCommand.getCommandWord().equals(commandWord))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Given an String, determine if that string is a charachter
     * @param String characterWord the string to be processed.
     * @return Character if the string represents a charachter in the room, otherwise return null
     */
    private Character getCharacterFromString(String characterWord) 
    {
        for (Character character : currentRoom.getCharacters()) 
        {
            if (characterWord.equals(character.getDescription())) 
            {
                return character;
            }
        }
        return null;
    }

    /**
     * Given a command, process whether the user wants to read a map or clue and then print out the information for that object
     * @param Command command the command from which the object is determined.     
     */
    private void printInfo(Command command) 
    {
        String itemWord = command.getSecondWord();
        if (getItemFromString(itemWord)!=null  && checkObjectCommand(command)) // checks if item being read actually exists or is available to user
        { 
            if (itemWord.equals("map")) //by Adam
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
            else
            {
                System.out.println("the ocean blue");
            }
        }
        else
        {
            System.out.println("You can't read that!");
        }
    }

    /**
     * Here we print out the items in the inventory and their weight
     */
    private void printInventory() 
    {
        if (inventory.size()>0) 
        {
            System.out.println("Current Inventory Items: ");
            for (Item item : inventory) 
            {
                System.out.println(item.getDescription() + ", weight: " + item.getWeight() + " ");
            }
        } else 
        {
            System.out.println("Inventory is empty.");
        }
    }

    /**
     * Here we print out all the info when a user enters a new room
     */
    private void printStatus() 
    {
        roomsVisited.add(currentRoom);
        nextRoom.setExit("back",roomsVisited.get(roomsVisited.size()-1));
        currentRoom = nextRoom;
        System.out.println();
        System.out.println("************************************************");        
        printInventory();
        currentRoom.printItemDescription();
        System.out.println();
        currentRoom.printCharacterDescription();
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
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

    /**
     * Given a command, determine the character that is being talked to, if that character is in the room, and print out their dialogue.
     * @param Command command the commannd from which the character is determined.     
     */
    private void talk(Command command)
    {
        if (currentRoom.getCharacters().isEmpty()) 
        {
            System.out.println("There are no characters here.");
        }
        else if (!command.hasSecondWord()) 
        {
            System.out.println("Please specify a character.");
        }
        else 
        {
            Character character = getCharacterFromString(command.getSecondWord());
            if (character != null) 
            {
                System.out.println("You approach the " + character.getDescription() + " and ask him '" + character.getQuestion()+"?'");
                System.out.println("The " + character.getDescription() + " replies: '"+character.getResponse()+".'");
                if (character == wizard) // wizard  curses the user after interaction
                {
                    if (!isSick)
                    {
                        makeSick();
                        System.out.println("The wizard curses you with a sickness");
                    }
                    transportCharacter(currentRoom,wineCellar,wizard); // wizard teleports if you meet them
                }
            } 
            else 
            {
                System.out.println("That is not a valid character for this room");
            }
        }
    }

    /**
     * Given a command, determine the item, and whether it exists and is food, then add the poisoned food to the inventory
     * @param Command command the commannd from which the food is determined.     
     */
    private void eat(Command command)
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        if (item == null || checkObjectCommand(command) == false) // make sure the object exists and is edible
        {
            System.out.println("You can't eat that!");
        }
        else
        {
            System.out.println("You eat the food, but quickly feel sick. It seems that it was poisoned");
            makeSick();
            add(command);
            System.out.println("If you do not find the antidote soon you will die!");
        }
    }

    /**
     * Given a command, determine the item, and whether it exists and is medicine, then cure the sickeness. If the user isn't sick don't let them drink the medicine.
     * @param Command command the commannd from which the medicine is determined.     
     */
    private void drink(Command command) 
    {
        if (checkObjectCommand(command) == false)
        {
            System.out.println("You can't drink that!");
        }
        else
        {
            if (isSick)
            {
                makeWell();
                System.out.println("The medicine seems to have worked. You feel much better");
            }

            else // make sure the user doesn't drink medicine if they are healthy
            {
                System.out.println("Before drinking the medicine you realize that is probably not the best idea and close the lid.");
                System.out.println("Maybe it will be useful later.");
            }
        }
    }

    /**
     * Given a command, determine the item, and whether it exists, then check that the inventory is not full and exists, then add the item to inventory,
     * so long as it doesn't exceed the capacity
     * @param Command command the commannd from which the item is determined.     
     */
    private void add(Command command) 
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        if (item!=null && item.getMovable()){
            if (isInventoryFull())
            {
                System.out.println("Cannot pick up item. Inventory is full");
            }
            else{
                if (!itemInInventory(item)){
                    inventory.add(item);
                    currentRoom.removeItem(item);
                    weight += item.getWeight();
                    System.out.println();
                    if (weight> INVENTORY_CAPACITY) // check that adding the weight does not exceed the carrying capacity 
                    {
                        weight -=item.getWeight();
                        System.out.println(item.getDescription()+" cannot be added to inventory, it weighs too much.");
                    }  
                    else
                    {
                        System.out.println(item.getDescription()+" added to inventory");
                    }
                    System.out.println("Current Inventory Weight: "+getInventoryWeight());
                    System.out.println();
                }
            }
        }
        else{
            System.out.println("You cannot add that object to inventory.");
        }
    }

    /**
     * Given a command, determine the item, and whether it exists, then remove the item and its weight from the inventory
     * @param Command command the command from which the item is determined.     
     */
    private void drop(Command command) 
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        weight -= item.getWeight();
        currentRoom.addItem(item);
        inventory.remove(item);
        System.out.println(item.getDescription()+" removed from inventory");
    }

    /**
     * Given a command, determine that item exists and is a key. If the user is in the entrance hall unlock the door, if not tell the user to go to the entrance hall
     * @param command the commannd from which the key item is determined.     
     */
    private void use(Command command) 
    {
        String itemWord = command.getSecondWord();
        if (getItemFromString(itemWord)!=null && checkObjectCommand(command))
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
    }

    /**
     * check that the inventory weight does not exceed capacity.    
     * @return true if inventory is full
     */
    private boolean isInventoryFull() 
    {
        if (getInventoryWeight()>=INVENTORY_CAPACITY)
        {
            return true;
        }
        return false;
    }

    /**
     * get the weight of all the items in the inventory  
     * @return int weight that is the sum of all the item weights.
     */
    private int getInventoryWeight() 
    {
        weight = 0;
        for (Item item : inventory) 
        {
            weight += item.getWeight();
        }
        return weight;
    }

    /**
     * Check that the door is not locked, and prompt user given room
     * @return true if door is locked 
     */
    private boolean checkLockedDoor() 
    {
        if (currentRoom.isLocked(direction))
        {
            if (currentRoom == dungeon)
            {
                if (passcodePrompt()) // a passcode that appears as a riddle for the user to solve
                {
                    return true;
                }
                return false;
            }
            else if (currentRoom == entranceHall)
            {
                if (openEntrance()) // user must find the key and use it to open the door
                {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Prompt user to try and unlock door
     * @return true if user successfully unlocks door
     */
    private boolean passcodePrompt() 
    {
        System.out.println("You try to open the door but find that it is locked!");
        System.out.println("You notice a 4 digit PIN number pad next to the door.");
        System.out.println("Enter Passcode: ");
        int code = parser.getInt();
        if (code == 1492) //answer to riddle
        { 
            System.out.println("You have unlocked the door. You proceed into the next room.");
            currentRoom.setIsLocked(false, "east");
            return true;
        }
        else
        {
            System.out.println("Incorrect code.");
            if (code==0) //arbitrary number to print error for entering a string
            {
                System.out.print("passcode must be a 4-digit PIN");
                System.out.println();
            }
            return false;
        }

    }

    /**
     * Prompt user to try and unlock door
     * @return true if user successfully unlocks door
     */
    private boolean openEntrance() 
    {
        if (usedKey)
        { 
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

    /**
     * Checks if the door exists 
     * @param Room nextRoom, the next room that user wants to go to.
     * @return true if door exists
     */
    private boolean checkNextRoom(Room nextRoom)
    {
        if (nextRoom == null) 
        {
            System.out.println("There is no door!");
            return false;
        }
        return true;
    }

    /** 
     * Try to go to one direction. If there is an unlocked exit, enter the new
     * room and print information, otherwise print an error message.
     * @param Command command, the command from which a direction is determined
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        direction = command.getSecondWord();
        nextRoom = currentRoom.getExit(direction);
        if (checkNextRoom(nextRoom)) //checks if door in that direction exists
        {
            if(checkLockedDoor()) // check if that door is locked
            {
                printStatus(); // prints stats and health updates when user goes to new room
                updateHealth();
                if (currentRoom==outside) // user wins game when they leave the mansion
                {
                    endGame("win");
                }
            }
        }
    }

    /** 
     * Randomly make user sick and update them on how much time they have left to live
     */
    private void updateHealth()
    {
        if(isSick) // update user about how much time they have left to survive
        {
            System.out.println("You have " + timeLeft + " turns left to find medicine before you die!");
            timeLeft --;
            if(timeLeft < 0)
            {
                System.out.println("You have died of sickness!");
                endGame("lose");
            }
        }
        else if(Math.random()<(0.05)) //randomly get sick
        {
            makeSick();
            System.out.println("You've been struck with a sudden sickness!");
        }
    }

    /** 
     * Makes the user sick and gives them 3 moves to find the cure
     */
    private void makeSick() 
    {
        isSick=true;
        timeLeft=3;
    }

    /** 
     * Makes the user well and gives them time to survive
     */
    private void makeWell() 
    {
        isSick=false;
        timeLeft=0;
    }

    /**
     * Transports the character to a different room following a conversation
     * @param Room currentRoom, the character's current room 
     * @param Room newRoom, the character's new room 
     * @param Character character, the character being transported
     */
    private void transportCharacter(Room currentRoom, Room newRoom, Character character) 
    {
        currentRoom.removeCharacter(character);
        newRoom.addCharacter(character);
    }

    /** 
     * Ends the game based on user actions
     * @param String condition, which determines wether user wins or loses
     */
    private void endGame(String condition) //by Adam
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
