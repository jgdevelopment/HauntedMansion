import java.util.ArrayList;
/**
 * Character class written by Adam Shaw, with inspiration from Jason's item class.
 * 
 * initializes characters with a preset conversation that they can engage in
 * with the game player. the format of the conversation is that the 
 * player asks the character a question unique to that character,
 * and the character responds with an answer unique to the character.
 * 
 * @author Adam Shaw 
 * @version October 2014
 */
public class Character

{
    //implementation    
    private String description;
    private String question;
    private String response;
    private ArrayList<Command> permissions;

    /**
     * Create a Character described "description" with a "question" and "response. Initially,
     * "description" is something like "ogre", "question" is something like "who are you", 
     * and "response" is something like "I am an ogre"
     * @param description The room's description.
     */
    public Character(String description, String question, String response)
    {
        permissions = new ArrayList<Command>();
        this.description = description;
        this.question = question;
        this.response = response;
        Command talk = new Command("talk", this.description);
        this.addPermissions(talk);
    }

    /**
     * Add a command that can be used on the character
     * @param Command command the command which is added to the character's allowed permissions, 
     * for now just talk (eventually maybe fight)
     */
    public void addPermissions(Command command)
    {
        // initialise instance variables
        this.permissions.add(command);
    }

    /**
     * returns the allowed commands for a character
     * @return permissions 
     */
    public ArrayList<Command> getPermission()
    {
        return this.permissions;
    }

    /**
     * returns the description for a character
     * @return description 
     */
    public String getDescription()
    {
        return this.description;
    }

     /**
     * returns the question for a character
     * @return question 
     */
    public String getQuestion()
    {
        return this.question;
    }

     /**
     * returns the response for a character
     * @return response 
     */
    public String getResponse()
    {
        return this.response;
    }
}