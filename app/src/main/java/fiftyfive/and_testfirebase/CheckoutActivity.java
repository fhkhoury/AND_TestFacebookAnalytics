package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Cart cart = new Cart();
    ListView mListView;
    ArrayList<Item> panier = new ArrayList<Item>();
    Bundle bundle4cart = new Bundle();
    Intent zeIntent = new Intent();
    Bundle firebaseTagBundle = new Bundle();
    String shippingMethodChosen ;
    String paymentMethodChosen ;
    String transactionId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        zeIntent = getIntent();
        bundle4cart = zeIntent.getBundleExtra("cart");
        cart = cart.transformBundleToCart(bundle4cart);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // send a hit to GA to log the screen name
        firebaseTagBundle.putString("screenName", "Checkout");
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "openScreen - Checkout sent.");
        Log.d("INFO: ", firebaseTagBundle.getString("screenName"));

        //Afficher totalAmount dans textView
        TextView totalAmount = (TextView)findViewById(R.id.totalAmount);
        assert totalAmount!= null;
        totalAmount.setText(cart.totalAmount.toString());

        //Générer num de commande aléatoire
        transactionId = String.valueOf(0 + (int)(Math.random() * ((999999 - 1) + 1)));



    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cb:
                if (checked)
                    paymentMethodChosen="card";
                    break;
            case R.id.radio_paypal:
                if (checked)
                    paymentMethodChosen="paypal";
                    break;
            case R.id.radio_oneDay:
                if (checked)
                    shippingMethodChosen="1_day";
                break;
            case R.id.radio_piority:
                if (checked)
                    shippingMethodChosen="priority";
                break;
            case R.id.radio_normal:
                if (checked)
                    shippingMethodChosen="normal";
                break;
        }
    }

    public void pay(View v){
        //Définir les user properties
        mFirebaseAnalytics.setUserProperty("payment_method", paymentMethodChosen);
        mFirebaseAnalytics.setUserProperty("shipping_method", shippingMethodChosen);
        //Envoyer le tag
        firebaseTagBundle.clear();
        firebaseTagBundle.putString(FirebaseAnalytics.Param.COUPON, "NONE");
        firebaseTagBundle.putString(FirebaseAnalytics.Param.CURRENCY, "EUR");
        firebaseTagBundle.putDouble(FirebaseAnalytics.Param.VALUE, cart.totalAmount);
        firebaseTagBundle.putDouble(FirebaseAnalytics.Param.TAX, (cart.totalAmount * 0.2));
        firebaseTagBundle.putDouble(FirebaseAnalytics.Param.SHIPPING, (cart.totalAmount * 0.05));
        firebaseTagBundle.putString("payment_method", paymentMethodChosen);
        firebaseTagBundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionId);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, firebaseTagBundle);
        zeIntent = new Intent(CheckoutActivity.this, PaymentConfirmationActivity.class);
        startActivity(zeIntent);
    }
}
