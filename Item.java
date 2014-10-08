import java.util.ArrayList;
/**
 * Write a description of class Items here.
 * 
 * @author class by JG
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - 
    private String description;
    private int weight;
    private ArrayList<Command> permissions;
    private boolean movable;

    /**
     * Constructor for objects of class Items
     */
    public Item(String description, int weight, boolean movable, String commandWord)
    {
        // initialise instance variables
        this.description = description;
        this.weight = weight;
        permissions = new ArrayList<Command>();
        if (movable){
            Command drop = new Command("drop", this.description);
            Command add = new Command("add", this.description);
            this.addPermissions(add);
            this.addPermissions(drop);
        }
        Command command = new Command(commandWord,this.description);
        this.addPermissions(command);
    }

    public boolean isValidCommand(String commandWord)
    {
        // this integer is used to determine 
        //if the command word matches any commands in the items permissions
        int anyCommand = 0; 
        for (Command iterateCommand :this.permissions) 
        {
            if (commandWord.equals(iterateCommand.getCommandWord()))
            {   
                anyCommand++;
            }
        }
        if (anyCommand==0) 
        {
            printInvalidCommand();
            return false;
        }
        return true;
    }

    private void printInvalidCommand()
    {
        System.out.println("You cannot use that command with this object");
    }

    public String getDescription(){
        return this.description;
    }

    public boolean getMovable(){
        return this.movable;
    }

    public void addPermissions(Command command)
    {
        // initialise instance variables
        this.permissions.add(command);
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
