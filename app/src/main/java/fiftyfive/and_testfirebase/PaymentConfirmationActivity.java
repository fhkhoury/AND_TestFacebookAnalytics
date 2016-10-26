package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.google.firebase.analytics.FirebaseAnalytics;

public class PaymentConfirmationActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Bundle firebaseTagBundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // send a hit to GA to log the screen name
        firebaseTagBundle.putString("screenName", "payment_confirmation");
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "openScreen - payment_confirmation sent.");
        Log.d("INFO: ", firebaseTagBundle.getString("screenName"));

    }

    public void kill(View v){
        Intent zeIntent = new Intent(PaymentConfirmationActivity.this, MainActivity.class);
        startActivity(zeIntent);
        moveTaskToBack(true);
    }
}
