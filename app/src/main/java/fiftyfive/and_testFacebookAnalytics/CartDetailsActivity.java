package fiftyfive.and_testFacebookAnalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static android.provider.Telephony.Mms.Part.CONTENT_ID;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CONTENT_ID;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CONTENT_TYPE;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CURRENCY;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_NUM_ITEMS;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE;
import static com.google.android.gms.analytics.internal.zzy.s;

public class CartDetailsActivity extends AppCompatActivity {

    Cart cart = new Cart();
    ListView mListView;
    ArrayList<Item> panier = new ArrayList<Item>();
    Bundle bundle4cart = new Bundle();
    Intent zeIntent = new Intent();
    Bundle FBTagBundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);


        bundle4cart = getIntent().getBundleExtra("cart");
        cart = cart.transformBundleToCart(bundle4cart);
        Log.d("ACTION: ", "Bundle récupéré");

        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.putString("screenName", "Cart details");
        logger.logEvent("screenView", FBTagBundle);
        Log.d("TAG: ", "screenView - Cart details sent.");
        Log.d("INFO: ", FBTagBundle.getString("screenName"));

        //les textviews
        TextView numberOfItems = (TextView)findViewById(R.id.numberOfItems);
        assert numberOfItems != null;
        numberOfItems.setText(cart.numberOfItems.toString());
        TextView totalAmount = (TextView)findViewById(R.id.totalAmount);
        assert totalAmount != null;
        totalAmount.setText("total amount");

        //TODO: Faire une interface de panier propre
        /*Récupérer  la listView
        mListView = (ListView) findViewById(R.id.listView);
        //remplir la liste avec le panier
        for (int i=0; i<cart.getNumberOfArticles(); i++){
            panier.add(cart.getItem(i));
        }

        //afficjer la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CartDetailsActivity.this, android.R.layout.simple_list_item_1, panier);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(CartDetailsActivity.this,simple_list_item_1, panier.toString());
        mListView.setAdapter(adapter);


        //Chap
        */
    }
    public void checkout(View v){
        //ENvoi du hit "checkout" FB en dur.
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.clear();
        FBTagBundle.putString(EVENT_PARAM_CONTENT_TYPE, "cart");
        FBTagBundle.putString(EVENT_PARAM_CONTENT_ID, "12345678");
        FBTagBundle.putString(EVENT_PARAM_NUM_ITEMS, cart.numberOfItems.toString());
        FBTagBundle.putString(EVENT_PARAM_CURRENCY, "EUR");
        FBTagBundle.putString(EVENT_PARAM_PAYMENT_INFO_AVAILABLE, "0");
        logger.logEvent(EVENT_NAME_INITIATED_CHECKOUT, cart.totalAmount, FBTagBundle);
        Log.d("TAG: ", "Checkout_initiated sent.");

        zeIntent = new Intent(CartDetailsActivity.this, CheckoutActivity.class);
        zeIntent.putExtra("cart", bundle4cart);
        startActivityForResult(zeIntent, 0);
    }
}
