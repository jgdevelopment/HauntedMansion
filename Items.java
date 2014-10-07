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
    public String description;
    public int weight;
    private ArrayList<Command> permissions;
    /**
     * Constructor for objects of class Items
     */
    public Items(String description)
    {
        // initialise instance variables
        permissions = new ArrayList<Command>();
        this.description = description;
        Command add = new Command("add", this.description);
        this.setPermissions(add);
        Command drop = new Command("drop", this.description);
        this.setPermissions(drop);
    }
    
    //can do command method, add permissions in item
    public void setPermissions(Command command)
    {
        // initialise instance variables
        this.permissions.add(command);
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public ArrayList<Command> getPermission(){
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
