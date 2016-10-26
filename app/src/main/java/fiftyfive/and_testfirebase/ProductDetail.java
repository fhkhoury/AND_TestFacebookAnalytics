package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Intent zeIntent = new Intent();
    Bundle bundle4Item = new Bundle();
    Bundle bundle4Cart = new Bundle();
    Item itemSelected = new Item();
    Cart cart = new Cart(); //TODO: pensez à récupérer le cart entre les activités
    Bundle firebaseTagBundle = new Bundle();
    Bundle gaTagBundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Retrieve the bundle parse from Product List and convert it to item
        zeIntent = getIntent();
        bundle4Item = zeIntent.getBundleExtra("selectedItem");
        bundle4Cart = zeIntent.getBundleExtra("cart");
        cart = cart.transformBundleToCart(bundle4Cart);



        Log.d("INFO: ", "item récupéré.");
        itemSelected = itemSelected.transformBundle2Item(bundle4Item);

        //Obtain the FirebaseAnalytics instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // FIre the Firebase Analytics tags
        firebaseTagBundle.putString("screenName", "Detail - " + itemSelected.name);
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "screenName sent.");
        Log.d("INFO; ", "Detail - " + itemSelected.name );
        itemViewFB();
        productViewGA();


        //les textviews
        TextView productName = (TextView)findViewById(R.id.name);
        assert productName != null;
        productName.setText(itemSelected.name);

        TextView productCategory = (TextView)findViewById(R.id.category);
        assert productCategory != null;
        productCategory.setText(itemSelected.category);

        TextView productVariant = (TextView)findViewById(R.id.variant);
        assert productVariant != null;
        productVariant.setText(itemSelected.variant);

        TextView productBrand = (TextView)findViewById(R.id.brand);
        assert  productBrand != null;
        productBrand.setText(itemSelected.brand);

        TextView productPrice = (TextView)findViewById(R.id.price);
        assert productPrice != null ;
        productPrice.setText(String.valueOf(itemSelected.price));


        Button buttonAddToCart = (Button)findViewById(R.id.button_addToCart);

        if (buttonAddToCart != null) {
            buttonAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cart.addItem(itemSelected);
                    // TODO : A refaire avec GTM
                    firebaseTagBundle.putString(FirebaseAnalytics.Param.PRODUCT_ID, itemSelected.sku);
                    firebaseTagBundle.putString(FirebaseAnalytics.Param.PRODUCT_NAME, itemSelected.name);
                    firebaseTagBundle.putString(FirebaseAnalytics.Param.PRODUCT_CATEGORY, itemSelected.category);
                    firebaseTagBundle.putDouble(FirebaseAnalytics.Param.PRICE, itemSelected.price);
                    firebaseTagBundle.putDouble(FirebaseAnalytics.Param.VALUE, itemSelected.price);
                    firebaseTagBundle.putString(FirebaseAnalytics.Param.QUANTITY, "1");
                    firebaseTagBundle.putString(FirebaseAnalytics.Param.CURRENCY, "EUR");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, firebaseTagBundle);
                    Toast.makeText(getApplicationContext(), itemSelected.name +" has been added to cart.", Toast.LENGTH_SHORT).show();
                    Log.d("TAG: ", "ADD_TO_CART sent.");
                }
            });
        }


        //if Cart non nul alors afficher le bouton View Cart
        if (cart != null){
            Button buttonViewCart = (Button)findViewById(R.id.button_viewCart);
            if (buttonViewCart != null) {
                //assert zeLayout != null;
                //zeLayout.addView(buttonViewCart, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                buttonViewCart.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        // TODO: A REVOIR
                        Intent zeIntent = new Intent(ProductDetail.this, CartDetailsActivity.class);
                        bundle4Cart = cart.transformCartToBundle();
                        zeIntent.putExtra("cart", bundle4Cart);
                        startActivityForResult(zeIntent, 0);
                    }
                });
            }
        }
    }

    public void itemViewFB(){
        firebaseTagBundle.clear();
        firebaseTagBundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemSelected.sku);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, firebaseTagBundle);
        Log.d("TAG: ", "VIEW_ITEM sent.");
    }

    public void productViewGA(){

        Bundle productsBundle = new Bundle();
        productsBundle.putString("name", itemSelected.name);
        productsBundle.putString("id", itemSelected.sku);
        productsBundle.putString("price", itemSelected.price.toString());
        productsBundle.putString("brand", itemSelected.brand);
        productsBundle.putString("category", itemSelected.category);
        productsBundle.putString("variant", itemSelected.variant);

        Bundle actionFieldBundle = new Bundle();
        actionFieldBundle.putString("list", itemSelected.category);
        actionFieldBundle.putBundle("products", productsBundle);

        Bundle detailBundle = new Bundle();
        detailBundle.putBundle("detail", actionFieldBundle);

        //envoi du tag e-commerce "detail" pour GA;
        gaTagBundle.clear();
        gaTagBundle.putBundle("ecommerce", detailBundle);
        mFirebaseAnalytics.logEvent("ecommerce", gaTagBundle);
        Log.d("TAG: ", "product view sent.");

        /* Measure a view of product details.
        dataLayer.push("ecommerce",
                DataLayer.mapOf(
                        "detail", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "Apparel Gallery"),               // detail actions have an optional list property.
                                "products", DataLayer.listOf(
                                  DataLayer.mapOf(
                                                "name", "Triblend Android T-Shirt",   // Name or ID is required.
                                                "id", "12345",
                                                "price", "15.25",
                                                "brand", "Google",
                                                "category", "Apparel",
                                                "variant", "Gray")))));
        */

    }


}
