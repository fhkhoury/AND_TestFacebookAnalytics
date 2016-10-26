package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.SimpleExpandableListAdapter;

/**
 * Created by Francois on 12/08/2016.
 */
public class Item implements Parcelable {

    String sku;
    String name;
    String category;
    String brand;
    String variant;
    Double price;

    public Item(String zeSku, String zeName, String zeCategory, String zeBrand, String zeVariant, Double zePrice){
        super();
        this.sku = zeSku;
        this.name = zeName;
        this.category = zeCategory;
        this.brand = zeBrand;
        this.variant = zeVariant;
        this.price = zePrice;
    }

    public Item(){

    }

    public Item(Parcel in) {
        this.sku= in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.brand = in.readString();
        this.variant = in.readString();
        this.price = in.readDouble();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(sku);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(brand);
        dest.writeString(variant);
        dest.writeDouble(price);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>()
    {
        @Override
        public Item createFromParcel(Parcel source)
        {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size)
        {
            return new Item[size];
        }
    };


    public static Parcelable.Creator<Item> getCreator()
    {
        return CREATOR;
    }


    public Bundle transformItem2Bundle(){
        Bundle returnedBundle = new Bundle();
        returnedBundle.putString("sku", sku);
        returnedBundle.putString("name", name);
        returnedBundle.putString("category", category);
        returnedBundle.putString("brand", brand);
        returnedBundle.putString("variant", variant);
        returnedBundle.putDouble("price", price);
        return returnedBundle;
    }

    public Item transformBundle2Item(Bundle itemBundled){
        Item zeItem = new Item(itemBundled.getString("sku"), itemBundled.getString("name"), itemBundled.getString("category"), itemBundled.getString("brand"), itemBundled.getString("variant"), itemBundled.getDouble("price"));
        return zeItem;
    }



    /*public Item bundleToItem(Bundle zeBundle){
        return new Item();
    }*/

    /*public Item getItemDetails(String itemName){
        Item item = new Item();
        switch (itemName) {
            case "Playstation - 400€":
                item.sku = "1234";
                item.name = itemName;
                item.category="console";
                item.brand = "Sony";
                item.variant="classic";
                item.price=399.99;
                break;
            case "X-Box - 300€":
                item.sku = "5678";
                item.name = itemName;
                item.category="console";
                item.brand = "Microsoft";
                item.variant="30 Go";
                item.price=295.99;
                break;
            case "PSP - 100€":
                item.sku = "1357";
                item.name = itemName;
                item.category="console";
                item.brand = "Sony";
                item.variant="Street version";
                item.price=99.99;
                break;
        }
        return item;
    }*/

}
