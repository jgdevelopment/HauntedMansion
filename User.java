import java.lang.Math;

/**
 * class User holds the inventory of the User. we also might update it in order to facilitate
 * iteraction with characters.
 * 
 * @author Adam Shaw
 * @version 2014.10.2
 */
public class User
{
    // the following are used as placeholders for static ints
    private static int INITIAL_INVENTORY_WEIGHT_CAPACITY=100;
    private static int SICK_RANDOM_CONDITION=20;

    private int weightCapacInv;
    private boolean isSick;
    /**
     * Constructor for objects of class User
     */
    public User()
    {
        this.setInventorySize(INITIAL_INVENTORY_WEIGHT_CAPACITY);
        this.makeWell();
    }

    public void sickRandomizer()
    {
        if(Math.random()<(1/SICK_RANDOM_CONDITION))
        {
            makeSick();
        }
    }
    
    public void sickRandomizerBiggerChance()
    {
        if(Math.random()<(10/SICK_RANDOM_CONDITION))
        {
            makeSick();
        }
    }
    
    public boolean returnSickCondition()
    {
        return this.isSick;
    }
    
    private void setInventorySize (int invWeight)
    {
        this.weightCapacInv = invWeight;
    }

    private int getInventorySize ()
    {
        return this.weightCapacInv;
    }

    private void makeSick()
    {
        this.isSick=true;
    }

    private void makeWell()
    {
        this.isSick=false;
    }
}