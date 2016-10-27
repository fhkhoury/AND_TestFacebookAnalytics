package fiftyfive.and_testFacebookAnalytics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {


    Spinner catalogue;
    Cart cart = new Cart();
    Bundle bundle4cart = cart.transformCartToBundle();

    Bundle FBTagParameters = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Track automatically AppInstalls and AppOpens
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        track_screenView();


    }




    //méthode pour faire partir un event après avoir cliqué sur un bouton
    //event codé pour utilisation via GTM et envoyé sur GA
    public void track_event(View v) {
        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        //Set the event parameters
        FBTagParameters.clear();
        FBTagParameters.putString("eventCategory", "clic");
        FBTagParameters.putString("eventAction", "fire");
        FBTagParameters.putString("eventLabel", "click2GA_GTM");
        //Log the event
        logger.logEvent("eventClick", FBTagParameters);
        Log.d("TAG: ", "track_event.");
        Toast.makeText(getApplicationContext(), "track_event sent.", Toast.LENGTH_SHORT).show();


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


    public void track_screenView(){
        //Create an instance of FB Analytics logger
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        //Set the event parameters
        FBTagParameters.clear();
        FBTagParameters.putString("screenName", "HomePage");
        //Log the event
        logger.logEvent("screenView", FBTagParameters);
        Log.d("TAG: ", "screenView - HomePage sent.");
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
