import java.util.ArrayList;
/**
 * Character class written by Adam Shaw, with inspiration from JG's item class.
 * 
 * initializes characters with a preset conversation that they can engage in
 * with the game player. the format of the conversation is that the 
 * player asks the character a question unique to that character,
 * and the character responds with an answer unique to the character.
 * 
 * Each character also has a set number of hitpoints, and the user can fight
 * him/her if the user chooses to do so.
 * 
 * *** though for now, characters must stay in one room,
 * *** a future update to the game might include the characters
 * *** being able to move from room to room!
 * 
 * @author Adam Shaw 
 * @version 2.1
 */
public class Character

{
    public String description;
    public String question;
    public String response;
    //     public int hitPoints;
    //     private boolean aliveQuestion=true;
    public ArrayList<Command> permissions;

    public Character(String description, String question, String response) //, int hitPoints
    {
        permissions = new ArrayList<Command>();

        this.description = description;
        this.question = question;
        this.response = response;
        //         this.hitPoints = hitPoints;
        // 
        //         Command fight = new Command("fight", this.description);
        Command talk = new Command("talk", this.description);

        //         this.setPermissions(fight);
        this.setPermissions(talk);
    }

    public void setPermissions(Command command)
    {
        // initialise instance variables
        this.permissions.add(command);
    }

    public ArrayList<Command> getPermission(){
        return this.permissions;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getQuestion()
    {
        return this.question;
    }

    public String getResponse()
    {
        return this.response;
    }

    //     public int getHitPoints()
    //     {
    //         return this.hitPoints;
    //     }
    // 
    //     public void kill()
    //     {
    //         aliveQuestion=false;
    //     }
    // 
    //     public boolean isAlive()
    //     {
    //         return aliveQuestion;
    //     }
}