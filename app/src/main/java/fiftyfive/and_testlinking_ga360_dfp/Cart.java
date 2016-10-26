package fiftyfive.and_testlinking_ga360_dfp;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Francois on 12/08/2016.
 */
public class Cart {

    Integer numberOfItems;
    Double totalAmount;
    ArrayList<Item> listeProduits;

    public  Cart(){
        super();
        this.numberOfItems=0;
        this.totalAmount=0.0;
        this.listeProduits = new ArrayList<Item>();
    }

    public void addItem(Item item){
        numberOfItems++;
        totalAmount += item.price;
        listeProduits.add(item);
    }

    public void deleteItem(){

    }

    public int getNumberOfArticles(){

        return numberOfItems;
    }

    public Item getItem(int index){

        return listeProduits.get(index);
    }

    public double totalAmount (){
        return totalAmount;
    }


    public Bundle transformCartToBundle(){
        Bundle cartBundled = new Bundle();
        cartBundled.putInt("numberOfItems", numberOfItems);
        cartBundled.putDouble("totalAmount",totalAmount);
        cartBundled.putParcelableArrayList("items", listeProduits);
        return cartBundled;
    }

    public Cart transformBundleToCart(Bundle cartBundled){
        Cart zeCart = new Cart();
        zeCart.numberOfItems = cartBundled.getInt("numberOfItems");
        zeCart.totalAmount= cartBundled.getDouble("totalAmount");
        zeCart.listeProduits = cartBundled.getParcelableArrayList("items");
        return zeCart;
    }

}
