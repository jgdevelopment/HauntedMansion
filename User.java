import java.lang.Math;
import java.util.ArrayList;
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
    public static ArrayList<Items> inventory;

    /**
     * Constructor for objects of class User
     */
    public User()
    {
        this.weight = 0;
        this.inventoryIsFull = false;
        inventory = new ArrayList<Items>();
    }

    public void addItem(Items item){
        this.weight += item.weight;
        inventory.add(item);
        if (this.weight> INVENTORY_CAPACITY){
            this.weight -=item.weight;
            this.inventoryIsFull = true;
        }    
    }

    public void removeItem(Items item){
        this.weight -= item.weight;
        inventory.remove(item);
        if (this.weight< INVENTORY_CAPACITY){
            this.inventoryIsFull = false;
        }    
    }

    public void makeSick()
    {
        this.isSick=true;
        this.timeLeft=3;
    }

    public String getInventoryItems()
    {
        String inventoryString = "";
        for (Items item: inventory){
            inventoryString+=" "+item.description;
        }
        return inventoryString;
    }

    public void makeWell()
    {
        this.isSick=false;
        this.timeLeft=0;
        System.out.println("The medicine seems to have worked. You feel much better");
    }
}