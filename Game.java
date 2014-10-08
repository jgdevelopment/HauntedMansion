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
    private static int INVENTORY_CAPACITY=100;
    private ArrayList<Item> inventory;
    private ArrayList<Room> roomsVisited;
    
    private boolean wantToQuit;
    private boolean isSick;
    private boolean usedKey;
    private int weight;
    private int timeLeft;
    
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
     * Create the game and initialise its internal map.
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
            "What's up",
            "The sky");
        wizard = new Character(
            "wizard",
            "What is the Answer to the ultimate question of life, the universe, and everything",
            "42");

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
            5,
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
        currentRoom.printItemDescription();
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
        if(command.isUnknown()) //first check command exists
        {
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("go")) //if command is "acting" on a direction
        {
            goRoom(command);
        }
        else if (commandWord.equals("talk")) // if the command is acting on an a character        
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
        else if (commandWord.equals("add")) 
        {
            add(command);
        }
        else if (commandWord.equals("eat")) 
        {
            eat(command);
        }
        else if (commandWord.equals("read")) 
        {
            printInfo(command);
        }
        else if (commandWord.equals("use")) 
        {
            use(command);
        }
        else if (commandWord.equals("drop")) 
        {
            drop(command);
        }
        else if (commandWord.equals("drink")) 
        {
            drink(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    private Item getItemFromString(String itemWord) 
    {
        for (Item item: inventory){
            if (itemWord.equals(item.getDescription())){
                return item;
            }
        }
        for (Item item: currentRoom.getItems()){
            if (itemWord.equals(item.getDescription())){
                return item;
            }
        }
        System.out.println("That object is not in this room."); 
        return null;
    }

    private Character getCharacterFromString(String characterWord) 
    {
        for (Character character : currentRoom.getCharacters()) {
            if (characterWord.equals(character.getDescription())) {
                return character;
            }
        }
        return null;
    }

    private void printInfo(Command command)
    {
        String itemWord = command.getSecondWord();
        if (getItemFromString(itemWord)!=null){
            if (itemWord.equals("map")){
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
            else{
                System.out.println("the ocean blue");
            }
        }
    }

    private void printInventory() {
        if (inventory.size()>0) {
            System.out.println("Current Inventory Items: ");
            for (Item item : inventory) {
                System.out.println(item.getDescription() + ":" + item.getWeight() + " ");
            }
        } else {
            System.out.println("Inventory is empty.");
        }
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

    private void talk(Command command)
    {
        if (currentRoom.getCharacters().isEmpty()) {
            System.out.println("There are no characters here.");
        }
        else if (!command.hasSecondWord()) {
            System.out.println("Please specify a character.");
        }
        else {
            Character character = getCharacterFromString(command.getSecondWord());
            if (character != null) {
                System.out.println("You approach the " + character.getDescription() + " and ask him '" + character.getQuestion()+"?'");
                System.out.println("The " + character.getDescription() + " replies: "+character.getResponse());
                if (character == wizard){
                    makeSick();
                    System.out.println("The wizard cursed you with a sickness");
                }
            } else {
                System.out.println("That is not a valid character for this room");
            }
        }
    }

    private void eat(Command command)
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        if (item == null){
            System.out.println("You can't eat that!");
        }
        for (Command allowedCommand: item.getPermission()){
            if (allowedCommand.getCommandWord().equals(itemWord)){
                System.out.println("You eat the food, but quickly feel sick. It seems that it was poisoned");
                makeSick();
                add(command);
                System.out.println("If you do not find the antidote soon you will die!");
                break;
            }
        }
    }

    private void drink(Command command)
    {
        if (getSickCondition())
        {
            makeWell();
        }
        else
        {
            System.out.println("Before drinking the medicine you realize that is probably not the best idea and close the lid.");
            System.out.println("Maybe it will be useful later.");
        }
    }

    /**
     * pickUp(Items item):
     * attempts to pick up an item and add it to the user's inventory.
     * if it is to heavy, the system prints a line telling the user the object cannot
     * be picked up
     */
    private void add(Command command)
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        if (item!=null){
            if (isInventoryFull())
            {
                System.out.println("Cannot pick up item. Inventory is full");
            }
            else{
                if (inventory!=null){
                    for (Item inventoryItem: inventory){
                        if (item.getDescription().equals(inventoryItem.getDescription())){
                            System.out.println("Item already added to inventory");
                            return;
                        }
                        else {
                        }
                    }
                }
                inventory.add(item);
                weight += item.getWeight();
                System.out.println(item.getDescription()+" added to inventory");
                if (weight> INVENTORY_CAPACITY){
                    weight -=item.getWeight();
                }    
                System.out.println("Current Inventory Weight: "+getInventoryWeight());
                System.out.println();
            }
        }
    }

    /**
     * drop(Command Command)
     * drops an item from the user's inventory, using a class inherited form the User Class.
     * this eliminates the item from the ArrayList<Items> inventory and removes that weight from 
     * the amount the user is currently carrying, and updates the inventory String to
     * reflect what the user is carrying
     */
    private void drop(Command command)
    {
        String itemWord = command.getSecondWord();
        Item item = getItemFromString(itemWord);
        System.out.println(item.getDescription()+" removed from inventory");
        removeItem(item);
    }

    private void use(Command command)
    {
        String itemWord = command.getSecondWord();
        if (getItemFromString(itemWord)!=null){
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

    private void removeItem(Item item)
    {
        weight -= item.getWeight();
        inventory.remove(item);
    }

    private boolean isInventoryFull()
    {
        if (getInventoryWeight()>=100)
        {
            return true;
        }
        return false;
    }

    private int getInventoryWeight()
    {
        weight = 0;
        for (Item item : inventory) 
        {
            weight += item.getWeight();
        }
        return weight;
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
        direction = command.getSecondWord();
        nextRoom = currentRoom.getExit(direction);
        if (checkNextRoom(command,nextRoom)) //checks if direction is valid
        {
            if(checkLockedDoor()) // if door is unlocked, then user can proceed to next room
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
                System.out.println();
                System.out.println(currentRoom.getLongDescription());
                System.out.println();
                if (currentRoom==outside)
                {
                    endGame("win");
                }
                updateHealth();
            }
        }

    }

    private void updateHealth()
    {
        if(Math.random()<(0.05)) //randomly get sick
        {
            makeSick();
            System.out.println("You've been struck with a sudden sickness!");
        }
        if(getSickCondition())
        {
            System.out.println("You have " + timeLeft + " turns left to find medicine before you die!");
            timeLeft --;
            if(timeLeft < 0)
            {
                System.out.println("You have died of sickness!");
                endGame("lose");
            }
        }
    }

    private void makeSick()
    {
        isSick=true;
        timeLeft=3;
    }

    private boolean getSickCondition(){
        return isSick;
    }

    private void makeWell()
    {
        isSick=false;
        timeLeft=0;
        System.out.println("The medicine seems to have worked. You feel much better");
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
