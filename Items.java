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
    private int anyCommand;
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

    public void setPermissions(Command command)
    {
        // initialise instance variables
        this.permissions.add(command);
    }

    public boolean isValidCommand(String commandWord)
    {
        // this integer is used to determine 
        //if the command word matches any commands in the items permissions
        anyCommand = 0; 
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

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getDescription(){
        return this.description;
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
