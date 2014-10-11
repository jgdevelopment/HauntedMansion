import java.util.ArrayList;
/**
 * Item class defines the methods for creating  an item and its usable commands.
 * 
 * @author Jason Ginsberg
 * @version October 2014
 */
public class Item
{
    // instance variables  
    private String description;
    private int weight;
    private ArrayList<Command> permissions;
    private boolean movable;

    /**
     * Constructor for objects of class Items, which has a description name, an inventory weight, 
     * may be added to inventory "movable", and a command to act on it.
     */
    public Item(String description, int weight, boolean movable, String commandWord)
    {
        // initialise instance variables
        this.description = description;
        this.weight = weight;
        this.movable = movable;
        permissions = new ArrayList<Command>();
        if (this.movable)
        {
            Command drop = new Command("drop", this.description);
            Command add = new Command("add", this.description);
            this.addPermissions(add);
            this.addPermissions(drop);
        }
        Command command = new Command(commandWord,this.description);
        this.addPermissions(command);
    }

    /**
     * returns the description for an item
     * @return description 
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * returns if an item can be added to inventory 
     * @return movable 
     */
    public boolean getMovable()
    {
        return this.movable;
    }

    /**
     * Add a command that can be used on the item
     * @param Command command the command which is added to the item's allowed permissions 
     */
    public void addPermissions(Command command)
    {
        this.permissions.add(command);
    }

    /**
     * returns the allowed commands for an item
     * @return permissions 
     */
    public ArrayList<Command> getPermission()
    {
        return this.permissions;
    }

    /**
     * returns an item's weight
     * @return weight 
     */
    public int getWeight()
    {
        return this.weight;
    }
}
