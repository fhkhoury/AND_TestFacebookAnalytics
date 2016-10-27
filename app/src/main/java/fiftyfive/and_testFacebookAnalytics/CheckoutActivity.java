package fiftyfive.and_testFacebookAnalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static android.provider.Telephony.Mms.Part.CONTENT_ID;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_PURCHASED;
import static com.facebook.appevents.AppEventsConstants.EVENT_NAME_VIEWED_CONTENT;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CONTENT_TYPE;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_CURRENCY;
import static com.facebook.appevents.AppEventsConstants.EVENT_PARAM_NUM_ITEMS;

public class CheckoutActivity extends AppCompatActivity {

    Cart cart = new Cart();
    ListView mListView;
    ArrayList<Item> panier = new ArrayList<Item>();
    Bundle bundle4cart = new Bundle();
    Intent zeIntent = new Intent();
    Bundle FBTagBundle = new Bundle();
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

        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.putString("screenName", "Checkout");
        logger.logEvent("screenView", FBTagBundle);
        Log.d("TAG: ", "screenView - Checkout sent.");
        Log.d("INFO: ", FBTagBundle.getString("screenName"));

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
        /* TODO : Définir les user properties Facebook
        mFirebaseAnalytics.setUserProperty("payment_method", paymentMethodChosen);
        mFirebaseAnalytics.setUserProperty("shipping_method", shippingMethodChosen);
        */


        //Envoi du tag "purchase" sur Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.clear();
        FBTagBundle.putString(EVENT_PARAM_CONTENT_TYPE, "order");
        FBTagBundle.putString(EVENT_PARAM_NUM_ITEMS, cart.numberOfItems.toString());
        FBTagBundle.putString(CONTENT_ID, transactionId);
        FBTagBundle.putString(EVENT_PARAM_CURRENCY, "EUR");
        FBTagBundle.putString("orderValue", cart.totalAmount.toString());
        //FBTagBundle.putString("shipping", cart.totalAmount * 0.05;
        FBTagBundle.putString("payment_method", paymentMethodChosen);
        logger.logEvent(EVENT_NAME_PURCHASED, cart.totalAmount*1.05, FBTagBundle);
        Log.d("TAG: ", EVENT_NAME_PURCHASED +" sent.");

        zeIntent = new Intent(CheckoutActivity.this, PaymentConfirmationActivity.class);
        startActivity(zeIntent);
    }
}
