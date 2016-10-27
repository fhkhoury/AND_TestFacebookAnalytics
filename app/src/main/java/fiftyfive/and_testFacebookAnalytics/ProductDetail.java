package fiftyfive.and_testFacebookAnalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import static android.R.attr.itemBackground;
import static android.R.attr.tag;
import static android.provider.Telephony.Mms.Part.CONTENT_ID;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_ADDED_TO_CART;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_VIEWED_CONTENT;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CONTENT_ID;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CONTENT_TYPE;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CURRENCY;
import static com.google.android.gms.analytics.internal.zzy.f;

public class ProductDetail extends AppCompatActivity {

    Intent zeIntent = new Intent();
    Bundle bundle4Item = new Bundle();
    Bundle bundle4Cart = new Bundle();
    Item itemSelected = new Item();
    Cart cart = new Cart(); //TODO: pensez à récupérer le cart entre les activités
    Bundle FBTagBundle = new Bundle();


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

        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.putString("screenName", "Detail - " + itemSelected.name);
        logger.logEvent("screenView", FBTagBundle);
        Log.d("TAG: ", "screenView sent.");
        Log.d("INFO; ", "Detail - " + itemSelected.name );
        //TODO: Ecrire le product view de FB
        track_itemView();


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

                    //Tracking du AddToCart
                    AppEventsLogger logger = AppEventsLogger.newLogger(ProductDetail.this);
                    FBTagBundle.clear();
                    FBTagBundle.putString(EVENT_PARAM_CONTENT_ID, itemSelected.sku);
                    FBTagBundle.putString(EVENT_PARAM_CONTENT_TYPE, "product");
                    FBTagBundle.putString(EVENT_PARAM_CURRENCY, "EUR");
                    FBTagBundle.putString("productName", itemSelected.name);
                    FBTagBundle.putString("productCategory", itemSelected.category);
                    FBTagBundle.putString("quantity", "1");
                    logger.logEvent(EVENT_NAME_ADDED_TO_CART, itemSelected.price, FBTagBundle);
                    Toast.makeText(getApplicationContext(), itemSelected.name +" has been added to cart.", Toast.LENGTH_SHORT).show();
                    Log.d("TAG: ", "ADDED_TO_CART sent.");
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

    public void track_itemView(){
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.putString(EVENT_PARAM_CONTENT_ID, itemSelected.sku);
        FBTagBundle.putString(EVENT_PARAM_CONTENT_TYPE, "product");
        FBTagBundle.putString("name", itemSelected.name);
        FBTagBundle.putString("price", itemSelected.price.toString());
        FBTagBundle.putString("brand", itemSelected.brand);
        FBTagBundle.putString("category", itemSelected.category);
        FBTagBundle.putString("variant", itemSelected.variant);
        logger.logEvent(EVENT_NAME_VIEWED_CONTENT, FBTagBundle);
        Log.d("TAG: ", EVENT_NAME_VIEWED_CONTENT+" sent.");
        Log.d("INFO; ", "Detail - " + itemSelected.name );
    }



}
