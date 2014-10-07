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

    private int weight;
    private boolean isSick;
    private boolean inventoryIsFull;
    private int timeLeft;
    private static ArrayList<Items> inventory;
    private String inventoryString;

    /**
     * Constructor for objects of class User
     */
    public User()
    {
        this.weight = 0;
        this.inventoryIsFull = false;
        inventory = new ArrayList<Items>();
        inventoryString = "empty";
    }

    public void addItem(Items item){
        if (this.inventory!=null){
            for (Items inventoryItem: this.inventory){
                if (item.description.equals(inventoryItem.description)){
                    System.out.println("Item already added to inventory");
                    return;
                }
            }
        }
        inventory.add(item);
        this.weight += item.weight;
        System.out.println(item.description+" added to inventory");
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
        inventoryString ="";
        for (Items item: inventory){
            inventoryString+=" "+item.description;
        }
        return inventoryString;
    }

    public Items checkItemPermission(String itemWord){
        for (Items item: inventory){
            if (itemWord.equals(item.description)){
                return item;
            }
        }
        return null;
    }

    public ArrayList<Items> getInventory()
    {
        return this.inventory;
    }

    public boolean isInventoryFull(){
        return this.inventoryIsFull;
    }

    public int getWeight(){
        return this.weight;
    }

    public void addWeight(int weightAdded){
        this.weight+=weightAdded;
    }

    public int getTimeLeft(){
        return timeLeft;
    }

    public void lostTime(){
        timeLeft --;
    }

    public boolean getSickCondition(){
        return isSick;
    }

    public void makeWell()
    {
        this.isSick=false;
        this.timeLeft=0;
        System.out.println("The medicine seems to have worked. You feel much better");
    }
}