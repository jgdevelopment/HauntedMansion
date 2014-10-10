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
 * @author Jason Ginsberg
 * @version 2014.10.07
 */

public class Room 
{
    private String description;
    private String lockedDirection;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> items;
    private ArrayList<Character> characters;
    private boolean isLocked;

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
        this.lockedDirection = "not set";
        this.isLocked = false;
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

    /**
     * adds an item to a room
     * @param Item item the item to be added
     */
    public void addItem(Item item) 
    {
        this.items.add(item);
    }

    /**
     * removes an item from a room
     * @param Item item the item to be removed
     */
    public void removeItem(Item item) 
    {
        this.items.remove(item);
    }

    /**
     * locks a room door
     * @param boolean value the boolean that sets wheter the door is locked or not
     * @param String direction the string that determines which door is locked
     */
    public void setIsLocked(boolean value, String direction)
    {
        this.isLocked = value;
        this.lockedDirection = direction;
    }

    /**
     * checks if room is locked at a direction
     * @return true if the room is locked at that direction
     * @param String direction the string that determines which door to check
     */
    public boolean isLocked(String direction)
    {
        if (this.lockedDirection.equals(direction)&&this.isLocked)
        {
            return true;
        }
        return false;
    }

    /**
     * returns items in the room
     * @return items in room
     */
    public ArrayList<Item> getItems() 
    {
        return this.items;     
    }

    /**
     * prints the items in the room
     */
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
            returnString += "You see in the room:";
            for (Item item : this.items){
                returnString += "\n"+item.getDescription();
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

    /**
     * prints the characters in the room
     */
    public void printCharacterDescription()
    {
        System.out.println("Characters in room: ");
        String characterString = null;
        for (Character character : this.characters){
            characterString += character.getDescription()+" "; 
            System.out.println(character.getDescription());
        }
        if (characterString==null){
            System.out.println("No characters");
        }
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

    /**
     * adds character to a room
     * @param Character character the character to be added to the room
     */
    public void addCharacter(Character character) 
    {
        this.characters.add(character);
    }    

    /**
     * removes character from a room
     * @param Character character the character to be removed from the room
     */
    public void removeCharacter(Character character) 
    {
        this.characters.remove(character);
    }    

    /**
     * returns characters in the room
     * @return characters in room
     */
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

