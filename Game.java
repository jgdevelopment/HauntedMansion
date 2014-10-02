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
    private Room currentRoom;

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
    drawingRoom;
    //initializations by Adam Shaw

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        //Initalize items and settings by JG
        Items book = new Items("book");
        book.setWeight(25); // out of 100
        Command read = new Command("read", "book");
        book.setPermissions(read);

        Items key = new Items("key");
        key.setWeight(5); // out of 100

        Items door = new Items("door");
        door.setWeight(101); // out of 100
        Command open = new Command("open", "door");
        door.setPermissions(open);

        Items food = new Items("food");
        food.setWeight(10); // out of 100
        Command eat = new Command("eat", "food");
        food.setPermissions(eat);

        Items closet = new Items("closet");
        closet.setWeight(101); // out of 100
        Command search = new Command("search", "closet");
        closet.setPermissions(search);

        //initializations by Adam Shaw
        Room masterBedroom,
        study,
        livingRoom,
        entranceHall, 
        library, 
        diningRoom,
        wineCellar, 
        dungeon,
        kitchen, 
        drawingRoom;

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
        masterBedroom.setItem(book);
        masterBedroom.setItem(closet);
        masterBedroom.setItem(door);

        study.setExit("west", masterBedroom);
        study.setExit("south", library);        
        study.setExit("east", livingRoom);
        study.setItem(book);

        livingRoom.setExit("west", study);
        livingRoom.setExit("south", diningRoom);
        livingRoom.setExit("east", entranceHall);

        entranceHall.setExit("west", livingRoom);
        entranceHall.setExit("north", outside);

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
        kitchen.setItem(door);

        drawingRoom.setExit("west",kitchen);

        dungeon.setExit("east",wineCellar);
        dungeon.setItem(key);
        dungeon.setItem(door);
        
        wineCellar.setExit("north",kitchen);
        wineCellar.setExit("west",dungeon);

        currentRoom = dungeon;  // start game in dungeon
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
        System.out.println("The floor is cold and damp.");
        System.out.println("You stand up but when you try to walk you suddenly trip and realize you're shackled to the floor");
        System.out.println("You're stuck in a dungeon. The room is dark. Try to escape.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());

    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("back")) {
            //go to previous room (either store info or reference room class)
        }
        else if (commandWord.equals("pick up")) {
            //add item to user weight and inventory for other commands like open
        }
        else if (commandWord.equals("open")) {
            // check if user has key
        }
        else if (commandWord.equals("eat")) {
            // print you are sick, the food was poisoned
            //change user so that they die unless they find medicine in 3 steps 
        }
        else if (commandWord.equals("read")) {
            System.out.println("Contents of Map");
        }
        else if (commandWord.equals("search")) {
            // prints if anything was found or if there is nothing there
            // if there is something found it asks user if they want to add it to inventory
            // weight of added object is added to user (done though item and user classes)
            // item is added to 
        }
        else if (commandWord.equals("drop")) {

        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are scared. You wander");
        System.out.println("around the haunted mansion.");
        System.out.println();
        System.out.println("You are not alone.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
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

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            if (currentRoom==outside)
            {
                winCondition();
            }
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

    private void winCondition()
    {
        System.out.println("You win!");
    }
}
