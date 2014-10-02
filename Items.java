import java.util.ArrayList;
/**
 * Write a description of class Items here.
 * 
 * @author class by JG
 * @version (a version number or a date)
 */
public class Items
{
    // instance variables - 
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
        Command pickUp = new Command("pick up", this.description);
        this.setPermissions(pickUp);
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
    public void dropItem(){ // maybe add to user class instead
        this.permissions = null;
        this.weight = 0;
    }
}
