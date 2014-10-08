import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author class by Jason Ginsberg
 * @version 2014.10.07
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> items;
    private boolean isLocked;
    private String lockedDirection;
    private ArrayList<Character> characters;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
        this.characters = new ArrayList<Character>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    public void addItem(Item item) 
    {
        this.items.add(item);
    }

    public void removeItem(Item item) 
    {
        this.items.remove(item);
    }

    public void setIsLocked(boolean value, String direction){
        this.isLocked = value;
        this.lockedDirection = direction;
    }

    public boolean isLocked(){
        return this.isLocked;
    }

    public String getLockedDirection(){
        return this.lockedDirection;
    }

    public ArrayList<Item> getItems() 
    {
        return this.items;     
    }

    public void printItemDescription() 
    {
        if (this.items.isEmpty())
        {
            System.out.println();
            System.out.println("There are no items here.");
        }
        else
        {
            String returnString ="\n";
            returnString += "You see in the room:\n";
            for (Item item : this.items){
                returnString += "\n"+item.getDescription()+"\n";
            }
            System.out.println(returnString);
        }
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return this.description;
    }

    public void printCharacterDescription()
    {
        System.out.println("Characters in room: ");
        for (Character character : this.characters){
            if (character!=null){
                System.out.println(character.getDescription());
            }
        }
        System.out.println("No characters");
        System.out.println();
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "\nYou are " + description + ".\n" + getExitString();
    }

    public void addCharacter(Character character) 
    {
        this.characters.add(character);
    }    

    public ArrayList<Character> getCharacters() 
    {
        return this.characters;
    }    

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
}

