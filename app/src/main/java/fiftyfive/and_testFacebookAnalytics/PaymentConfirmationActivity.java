package fiftyfive.and_testFacebookAnalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

public class PaymentConfirmationActivity extends AppCompatActivity {

    Bundle FBTagBundle = new Bundle();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        FBTagBundle.putString("screenName", "payment_confirmation");
        logger.logEvent("screenView", FBTagBundle);
        Log.d("TAG: ", "screenView - payment_confirmation sent.");
        Log.d("INFO: ", FBTagBundle.getString("screenName"));

    }

    public void kill(View v){
        Intent zeIntent = new Intent(PaymentConfirmationActivity.this, MainActivity.class);
        startActivity(zeIntent);
        moveTaskToBack(true);
    }
}
