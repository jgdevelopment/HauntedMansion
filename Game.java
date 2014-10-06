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
    private Parser parser;
    public static Room currentRoom;
    private User user;
    private ArrayList<Room> roomsVisited;
    private String direction;
    //room initializations by Adam Shaw
    Room masterBedroom,
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

    Character ogre, wizard;

    boolean usedKey;

    Items map,
    key,
    food,
    closet,
    hint;

    private static int CONDITION=20;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        user = new User();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        //Initalize items and settings by JG
        roomsVisited = new ArrayList<Room>();
        ogre = new Character(
            "ogre",
            "What's up",
            "The sky",
            30);

        wizard = new Character(
            "wizard",
            "What is the Answer to the Ultimate Question of Life, The Universe, and Everything",
            "42",
            60);

        usedKey = false;

        map = new Items("map");
        map.setWeight(25); // out of 100
        Command readMap = new Command("read", "map");
        map.setPermissions(readMap);

        key = new Items("key");
        key.setWeight(5); // out of 100
        Command use = new Command("use", "food");
        key.setPermissions(use);

        food = new Items("food");
        food.setWeight(10); // out of 100
        Command eat = new Command("eat", "food");
        food.setPermissions(eat);

        hint = new Items("clue");
        hint.setWeight(5); // out of 100
        Command readHint = new Command("read", "clue");
        hint.setPermissions(readHint);

        // create the rooms
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

        // initialise room exits and items in room
        masterBedroom.setExit("east", study);
        masterBedroom.setItem(closet);

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

        outside.setExit("south", entranceHall);

        library.setExit("north", study);
        library.setExit("east", diningRoom);

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
        roomsVisited.add(dungeon);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
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
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        if(command.isUnknown()){
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        Items item = parser.getCommandItem();
        if (commandWord.equals("go")) {
            direction = command.getSecondWord();
            nextRoom = currentRoom.getExit(direction);
            if (checkNextRoom(command,nextRoom)){
                if(checkLockedDoor()){
                    goRoom(command);
                }
            }
        }

        else if (item==null){
            if (commandWord.equals("help")) {
                printHelp();
            }
            else if (commandWord.equals("quit")) {
                wantToQuit = quit(command);
            }
            else{
                System.out.println("That is not an object.");
                return false;
            }
        }
        else{
            if (item.permissions.contains(command)){
                System.out.println("You cannot use that command with this object");
                return false;
            }

            else if (commandWord.equals("add")) {
                pickUp(item);
            }
            else if (commandWord.equals("eat")) {
                eat();
            }
            else if (commandWord.equals("read")) {
                if (command.getSecondWord().equals("map")){ //error can always open map
                    printMap();
                }
                else if (command.getSecondWord().equals("clue")){
                    System.out.println("the ocean blue");
                }
            }
            else if (commandWord.equals("use")) {
                use();
            }
            else if (commandWord.equals("drop")) {
                drop(item);
            }
            else if (commandWord.equals("s")) {
                //swallow pill for antidote
            }
        }
        // else command not recognised.
        return wantToQuit;
    }

    private void printMap() 
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

    private void pickUp(Items item){
        if (this.user.inventoryIsFull){
            System.out.println("Cannot pick up item. Inventory is full");
        }
        else{
            //add item to user weight and inventory for other commands like open,
            System.out.println(item.description+" added to inventory");
            user.addItem(item);
        }
    }

    public void talk(Character character)
    {
        System.out.println("You approach the " + character.getDescription() + " and ask him '" + character.getQuestion()+"?'");
        System.out.println("The" + character.getDescription() + " replies: '"+".'");        
        System.out.println(character.getResponse());        
    }

    private void use(){
        usedKey = true;
        System.out.println("You insert the key. Now try to open the door.");
    }

    private void drop(Items item){
        System.out.println(item.description+" removed from inventory");
        user.removeItem(item);
    }

    private void eat(){
        System.out.println("You eat the food, but quickly feel sick. It seems that it was poisoned");
        user.makeSick();
        user.weight += food.getWeight();
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

    private boolean checkLockedDoor(){
        if (currentRoom.isLocked && direction.equals(currentRoom.getLockedDirection())){
            if (currentRoom == dungeon){
                System.out.println("You try to open the door but find that it is locked!");
                System.out.println("You notice a 4 digit PIN number pad next to the door.");
                System.out.println("Enter Passcode: ");
                int code= parser.getInt();
                if (code == 1492){ //answer to riddle
                    System.out.println("You have unlocked the door. You proceed into the next room.");
                    currentRoom.setIsLocked(false, "east");
                    return true;
                }
                else{
                    System.out.println("Incorrect code.");
                    return false;
                }
            }
            else if (currentRoom == entranceHall){
                if (usedKey){ //answer to riddle
                    System.out.println("You have unlocked the door. You escape outside.");
                    currentRoom.setIsLocked(false, "north");
                    return true;
                }
                else{
                    System.out.println("Cannot open door. It seems to be locked.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkNextRoom(Command command, Room nextRoom){
        if (nextRoom == null) {
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
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        roomsVisited.add(currentRoom);
        nextRoom.setExit("back",roomsVisited.get(roomsVisited.size()-1));
        currentRoom = nextRoom;
        System.out.println(currentRoom.getLongDescription());
        System.out.println("Current Inventory Items: "+user.getInventoryItems());
        System.out.println("Current Inventory Weight: "+user.weight);
        if (currentRoom==outside)
        {
            endGame("win");
        }
        updateHealth();
    }

    private void updateHealth(){
        if(Math.random()<(1/CONDITION)) //randomly get sick
        {
            user.makeSick();
        }
        if(user.isSick)
        {
            System.out.println("You have " +  user.timeLeft + " turns left to find medicine before you die!");
            user.timeLeft--;
            if(user.timeLeft<0)
            {
                System.out.println("You have died of sickness!");
                endGame("lose");
            }
        }
    }

    private void endGame(String condition)
    {
        if (condition.equals("win")){
            System.out.println("You win!");
        }
        else{
            System.out.println("You lose!");
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
