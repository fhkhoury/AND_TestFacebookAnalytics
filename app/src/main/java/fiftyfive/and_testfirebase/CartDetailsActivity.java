package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class CartDetailsActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Cart cart = new Cart();
    ListView mListView;
    ArrayList<Item> panier = new ArrayList<Item>();
    Bundle bundle4cart = new Bundle();
    Intent zeIntent = new Intent();
    Bundle firebaseTagBundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);


        bundle4cart = getIntent().getBundleExtra("cart");
        cart = cart.transformBundleToCart(bundle4cart);
        Log.d("ACTION: ", "Bundle récupéré");

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // send a hit to GA to log the screen name
        firebaseTagBundle.putString("screenName", "Cart details");
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "openScreen - Cart details sent.");
        Log.d("INFO: ", firebaseTagBundle.getString("screenName"));

        //les textviews
        TextView numberOfItems = (TextView)findViewById(R.id.numberOfItems);
        assert numberOfItems != null;
        numberOfItems.setText(cart.numberOfItems.toString());
        TextView totalAmount = (TextView)findViewById(R.id.totalAmount);
        assert totalAmount != null;
        totalAmount.setText("total amount");

        //TODO: Faire une interface de panier propre
        /*//Récupérer  la listView
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
        //ENvoi du hit
        firebaseTagBundle.clear();
        firebaseTagBundle.putString(FirebaseAnalytics.Param.VALUE, cart.totalAmount.toString());
        firebaseTagBundle.putString(FirebaseAnalytics.Param.CURRENCY, "EUR");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, firebaseTagBundle);
        Log.d("TAG: ", "BEGIN_CHECKOUT sent.");
        zeIntent = new Intent(CartDetailsActivity.this, CheckoutActivity.class);
        zeIntent.putExtra("cart", bundle4cart);
        startActivityForResult(zeIntent, 0);
    }
}
