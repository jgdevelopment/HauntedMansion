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

    public int weight;
    public boolean isSick;
    public boolean inventoryIsFull;
    public int timeLeft;

    /**
     * Constructor for objects of class User
     */
    public User()
    {
        this.weight = 0;
        this.inventoryIsFull = false;
    }

    public void addItem(Items item){
        this.weight += item.weight;
        if (this.weight< INVENTORY_CAPACITY){
            this.weight -=item.weight;
            this.inventoryIsFull = true;
        }    
    }

    public void makeSick()
    {
        this.isSick=true;
        this.timeLeft=3;
    }

    public void makeWell()
    {
        this.isSick=false;
        this.timeLeft=0;
    }
}