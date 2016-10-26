package fiftyfive.and_testfirebase;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static com.google.firebase.crash.FirebaseCrash.*;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics ;
    Spinner catalogue;
    Cart cart = new Cart();
    public final static String SUPERBUNDLE = "DataLayer";
    Bundle bundle4cart = cart.transformCartToBundle();

    Bundle firebaseTagBundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);

        trackAPP_OPEN();

    }




    //méthode pour faire partir un event après avoir cliqué sur un bouton
    //event codé pour utilisation via GTM et envoyé sur Firebase & GA
    public void click2Fire_GTM(View v) {
        firebaseTagBundle.clear();
        firebaseTagBundle.putString("eventCategory", "clic");
        firebaseTagBundle.putString("eventAction", "fire");
        firebaseTagBundle.putString("eventLabel", "click2Fire_GTM");
        mFirebaseAnalytics.logEvent("click2Fire_GTM", firebaseTagBundle);
        Log.d("TAG: ", "Click2FIre_GTM sent.");
        Toast.makeText(getApplicationContext(), "Click2FIre_GTM sent.", Toast.LENGTH_SHORT).show();


    }

    // Make a crash
    public void crash(View v){
        report(new Exception("My first Android non-fatal error"));
        FirebaseCrash.log("App has crashed");
        Log.d("CRASH: ", "App has crashed, Buddy!");
        Toast.makeText(getApplicationContext(), "App has crashed, Buddy!", Toast.LENGTH_SHORT).show();
    }


    //View product list
    public void viewProductsList(View v){
        Intent zeIntent = new Intent(MainActivity.this, ProductsListActivity.class);
        zeIntent.putExtra("cart", bundle4cart);
        startActivityForResult(zeIntent, 0);
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    public void trackAPP_OPEN(){
        // send a hard-coded hit to FB when the app is opened
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, firebaseTagBundle);
        Log.d("TAG: ", "APP_OPEN sent.");
    }

    public void trackopenScreen(){
        //send a hit to GA to log the screen name
        firebaseTagBundle.clear();
        //firebaseTagBundle.putString(FirebaseAnalytics.Param.VALUE, "HomePage");// TODO: Check si ça fonctionne
        firebaseTagBundle.putString("screenName", "HomePage");
        mFirebaseAnalytics.logEvent("openScreen", firebaseTagBundle);
        Log.d("TAG: ", "openScreen - HomePage sent.");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
