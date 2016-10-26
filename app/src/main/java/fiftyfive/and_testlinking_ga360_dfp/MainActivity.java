package fiftyfive.and_testlinking_ga360_dfp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;

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
        track_screenView();
        //trackAPP_OPEN();

    }




    //méthode pour faire partir un event après avoir cliqué sur un bouton
    //event codé pour utilisation via GTM et envoyé sur GA
    public void click2GA_GTM(View v) {
        firebaseTagBundle.clear();
        firebaseTagBundle.putString("eventCategory", "clic");
        firebaseTagBundle.putString("eventAction", "fire");
        firebaseTagBundle.putString("eventLabel", "click2GA_GTM");
        mFirebaseAnalytics.logEvent("eventClick", firebaseTagBundle);
        Log.d("TAG: ", "Click2GA_GTM sent.");
        Toast.makeText(getApplicationContext(), "Click2GA_GTM sent.", Toast.LENGTH_SHORT).show();


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

    /*public void trackAPP_OPEN(){
        // send a hard-coded hit to FB when the app is opened
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, firebaseTagBundle);
        Log.d("TAG: ", "APP_OPEN sent.");
    }*/

    public void track_screenView(){

        firebaseTagBundle.clear();
        firebaseTagBundle.putString("screenName", "HomePage");
        mFirebaseAnalytics.logEvent("screenView", firebaseTagBundle);
        Log.d("TAG: ", "screenView - HomePage sent.");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
