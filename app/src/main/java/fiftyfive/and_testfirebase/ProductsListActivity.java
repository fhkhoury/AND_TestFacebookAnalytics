package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsListActivity extends AppCompatActivity {

    ListView availableProducts ;
    private FirebaseAnalytics mFirebaseAnalytics;
    ArrayList<Item> produitsDispo  = new ArrayList<Item>();
    Bundle firebaseTagBundle = new Bundle();
    Bundle gaTagBundle = new Bundle();
    Cart cart = new Cart();
    Bundle bundle4cart = new Bundle();
    Intent zeIntent = new Intent();
    Bundle bundle4SelectedItem = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        //récupération du panier
        bundle4cart = getIntent().getBundleExtra("cart");
        cart = cart.transformBundleToCart(bundle4cart);
        Log.d("ACTION: ", "Panier récupéré");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Obtain the GTM Legacy Datalyaer
        DataLayer dataLayer = TagManager.getInstance(this).getDataLayer();

        //Création & remplissage de la liste de produits proposés

        produitsDispo = fillCatalogue(produitsDispo);
        //check relecture
        for (int j=0; j<produitsDispo.size();j++) {
            //Log.d("POSITION : ", bundleImpressions.get(j).getInt("position"));
            Log.d("NAME : ", produitsDispo.get(j).name);
            Log.d("ID : ", produitsDispo.get(j).sku);
            Log.d("PRICE : ", produitsDispo.get(j).price.toString());
            Log.d("BRAND : ", produitsDispo.get(j).brand);
        }

        //Récupération de la listview créée dans le fichier activity_products_list.xml
        availableProducts = (ListView) findViewById(R.id.listviewproducts);

        //Création de la ArrayList qui nous permettra de remplire la listView
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

        //On déclare la HashMap qui contiendra les informations pour un item
        HashMap<String, String> map;

        for (Integer i=0; i<produitsDispo.size(); i++){
            //Création d'une HashMap pour insérer les informations du premier item de notre listView
            map = new HashMap<String, String>();
            //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
            map.put("name", produitsDispo.get(i).name);
            //on insère un élément description que l'on récupérera dans le textView description créé dans le fichier affichageitem.xml
            map.put("price", produitsDispo.get(i).price.toString()+ "€");
            listItem.add(map);
        }

        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
        SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.affichageitem,
                new String[] {"name", "price"}, new int[] {R.id.name, R.id.price});

        //On attribut à notre listView l'adapter que l'on vient de créer
        availableProducts.setAdapter(mSchedule);

        //Enfin on met un écouteur d'évènement sur notre listView
        final ArrayList<Item> finalProduitsDispo = produitsDispo;
        availableProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                HashMap<String, String> map = (HashMap<String, String>) availableProducts.getItemAtPosition(position);
                zeIntent = new Intent(ProductsListActivity.this, ProductDetail.class);
                bundle4SelectedItem = produitsDispo.get(position).transformItem2Bundle();
                trackProductClick(produitsDispo.get(position));
                zeIntent.putExtra("selectedItem", bundle4SelectedItem); // pour passer le data laye
                zeIntent.putExtra("cart", bundle4cart);
                startActivityForResult(zeIntent, 0);

            }
        });

        trackopenScreen();
        trackVIEW_ITEM_LIST(dataLayer);
    }

    public ArrayList<Item> fillCatalogue(ArrayList<Item> catalogue){
        catalogue.add(new Item("123456", "Playstation 4", "Console de salon", "Sony Corporation", "Uncharted edition", 399.99));
        catalogue.add(new Item("098765", "Xbox One", "Console de salon", "Microsoft", "30 Go", 290.00));
        catalogue.add(new Item("135791", "PSP Street", "Console portable", "Sony Corporation", "PSP Street Fifa 16 Edition", 99.90));
        return catalogue;
    }

    public void trackopenScreen(){
        firebaseTagBundle.putString("screenName", "ListeProduits - console");
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "screenName sent.");
    }

    public void trackVIEW_ITEM_LIST(DataLayer dataLayer){
        firebaseTagBundle.clear();
        //envoi du tag e-commerce "viewList" pour FB
        firebaseTagBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "console");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, firebaseTagBundle);
        Log.d("TAG: ", "VIEW_ITEM_LIST sent.");


        //envoi du tag e-commerce "viewList" pour GA via GTM v4 DataLayer;
                DataLayer.mapOf(
                        "currencyCode", "EUR",                                  // Local currency is optional.
                        "impressions", DataLayer.listOf(
                                DataLayer.mapOf(
                                        "name", produitsDispo.get(0).name,             // Name or ID is required.
                                        "id", produitsDispo.get(0).sku,
                                        "price", produitsDispo.get(0).price,
                                        "brand", produitsDispo.get(0).brand,
                                        "category", produitsDispo.get(0).category,
                                        "variant", produitsDispo.get(0).variant,
                                        "list", produitsDispo.get(0).category,
                                        "position", 1),
                                DataLayer.mapOf(
                                        "name", produitsDispo.get(1).name,
                                        "id", produitsDispo.get(1).sku,
                                        "price", produitsDispo.get(1).price,
                                        "brand", produitsDispo.get(1).brand,
                                        "category", produitsDispo.get(1).category,
                                        "variant", produitsDispo.get(1).variant,
                                        "list", produitsDispo.get(1).category,
                                        "position", 2),
                                DataLayer.mapOf(
                                        "name", produitsDispo.get(2).name,
                                        "id", produitsDispo.get(2).sku,
                                        "price", produitsDispo.get(2).price,
                                        "brand", produitsDispo.get(2).brand,
                                        "category", produitsDispo.get(2).category,
                                        "variant", produitsDispo.get(2).variant,
                                        "list", produitsDispo.get(2).category,
                                        "position", 3)));

        //envoi du tag e-commerce "viewList" pour GA via GTM v5 Bundle;
        //gaTagBundle.putString("currencyCode", "EUR");
        //gaTagBundle.putParcelableArrayList("impressions", constructBundleImpressions(produitsDispo));

        //mFirebaseAnalytics.logEvent("ecommerce", gaTagBundle);
        Log.d("TAG: ", "e-commerce-impressions sent.");
    }

    public void trackProductClick(Item item){

        Bundle productsBundle = new Bundle();
        productsBundle.putString("name", item.name);
        productsBundle.putString("id", item.sku);
        productsBundle.putString("price", item.price.toString());
        productsBundle.putString("brand", item.brand);
        productsBundle.putString("category", item.category);
        productsBundle.putString("variant", item.variant);

        Bundle actionFieldBundle = new Bundle();
        actionFieldBundle.putString("list", item.category);
        actionFieldBundle.putBundle("products", productsBundle);

        Bundle clickBundle = new Bundle();
        clickBundle.putBundle("click", actionFieldBundle);

        //Bundle ecommerceBundle = new Bundle();
        //ecommerceBundle.putBundle("ecommerce", clickBundle);

        //envoi du tag e-commerce "click" pour GA;
        gaTagBundle.clear();
        gaTagBundle.putBundle("ecommerce", clickBundle);
        mFirebaseAnalytics.logEvent("productClick", gaTagBundle);
        Log.d("TAG: ", "e-commerce-productClick sent.");

    }

    public ArrayList<Bundle> constructBundleImpressions(ArrayList<Item> produitsDispo){
        ArrayList<Bundle> bundleImpressions = new ArrayList<Bundle>();
        Bundle tempBundle = new Bundle();
        for (int i=0; i<produitsDispo.size();i++){
            tempBundle.clear();
            tempBundle.putString("name", produitsDispo.get(i).name);
            Log.d("AAAAA ; ", produitsDispo.get(i).name);
            tempBundle.putString("id", produitsDispo.get(i).sku);
            tempBundle.putString("price", produitsDispo.get(i).price.toString());
            tempBundle.putString("brand", produitsDispo.get(i).brand);
            tempBundle.putString("category", produitsDispo.get(i).category);
            tempBundle.putString("variant", produitsDispo.get(i).variant);
            tempBundle.putString("list", produitsDispo.get(i).category);
            tempBundle.putInt("position", i+1);
            bundleImpressions.add(tempBundle);

        }

        //check relecture
        for (int j=0; j<bundleImpressions.size();j++) {
            //Log.d("POSITION : ", bundleImpressions.get(j).getInt("position"));
            Log.d("NAME : ", bundleImpressions.get(j).getString("name"));
            Log.d("ID : ", bundleImpressions.get(j).getString("id"));
            Log.d("PRICE : ", bundleImpressions.get(j).getString("price"));
            Log.d("BRAND : ", bundleImpressions.get(j).getString("brand"));
        }
        return bundleImpressions;



    }

}
