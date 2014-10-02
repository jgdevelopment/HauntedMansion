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
    // instance variables - replace the example below with your own
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
    }

    private void setInventorySize (int invWeight)
    {
        this.weightCapacInv = invWeight;
    }

    private int getInventorySize ()
    {
        return this.weightCapacInv;
    }

    private void sickRandomizer()
    {
        if(Math.random()<(1/SICK_RANDOM_CONDITION))
        {
            makeSick();
        }
    }

    private void makeSick()
    {
        this.isSick=true;
    }

    private void makeWell()
    {
        this.isSick=false;
    }

    private boolean returnSickCondition()
    {
        return this.isSick;
    }
}