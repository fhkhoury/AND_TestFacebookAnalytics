package fiftyfive.and_testfirebase;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import static com.google.firebase.crash.FirebaseCrash.*;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // send a hit when the app is opened
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
        // envoyer un tag d'écran via GTM vers Firebase & GA
        bundle.clear();
        bundle.putString("screenName","François");
        mFirebaseAnalytics.logEvent("openScreen", bundle);

    }

    //méthode pour faire partir un event après avoir cliqué sur un bouton
    //event codé en dur et envoyé sur Firebase
    public void click2Fire_hard(View v) {
        Bundle myBundle = new Bundle();
        mFirebaseAnalytics.logEvent("click2Fire_hard", myBundle);
        Log.d("click2Fire_hard", "event sent");
    }

    //méthode pour faire partir un event après avoir cliqué sur un bouton
    //event codé pour utilisation via GTM et envoyé sur Firebase & GA
    public void click2Fire_GTM(View v) {
        Bundle thisBundle = new Bundle();
        thisBundle.putString("eventCategory", "clic");
        thisBundle.putString("eventAction", "fire");
        thisBundle.putString("eventLabel", "click2Fire_GTM");
        mFirebaseAnalytics.logEvent("click2Fire_GTM", thisBundle);
        Log.d("click2Fire_GTM", "event sent");
    }

    //Send a notification
    public void sendNotification(View v) {
        //MyFirebaseMessagingService.sendNotification("Mon MEssage");
    }

    // Make a crash
    public void crash(View v){
        report(new Exception("My first Android non-fatal error"));
        FirebaseCrash.log("App is crashed");
        FirebaseCrash.log(getPhoneNumber());

    }

    public String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();

    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
