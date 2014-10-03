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
    private static int INVENTORY_CAPACITY=100;
    private static int CONDITION=20;

    private int weight;
    private boolean isSick;
    public boolean inventoryIsFull;

    /**
     * Constructor for objects of class User
     */
    public User()
    {
        this.weight = 0;
        this.inventoryIsFull = false;
    }

    private void addItem(Items item){
        this.weight += item.weight;
        if (this.weight< INVENTORY_CAPACITY){
            this.weight -=item.weight;
            this.inventoryIsFull = true;
        }    
    }

    private void sickRandomizer()
    {
        if(Math.random()<(1/CONDITION))
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