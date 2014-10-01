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
    private int weight;
    private ArrayList<String> permissions;
    /**
     * Constructor for objects of class Items
     */
    public Items(String description)
    {
        // initialise instance variables
        this.description = description;
    }
    public void setPermissions(Command command)
    {
        // initialise instance variables
        String commandWord = command.getCommandWord();
        this.permissions.add(commandWord);
    }
    public void setWeight(int weightInput){
        this.weight = weight;
    }
    public ArrayList<String> getPermission(){
        return this.permissions;
    }
    public int getWeight(){
        return this.weight;
    }
}
