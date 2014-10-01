import java.util.ArrayList;
/**
 * Write a description of class Items here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Items
{
    // instance variables - replace the example below with your own
    private String description;
    private ArrayList<String> permissions;
    /**
     * Constructor for objects of class Items
     */
    public Items(String description)
    {
        // initialise instance variables
        this.description = description;
    }
    public void setPermissions(String permission)
    {
        // initialise instance variables
        this.permissions.add(permission);
    }
}
