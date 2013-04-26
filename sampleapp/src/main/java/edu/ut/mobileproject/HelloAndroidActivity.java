package edu.ut.mobileproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import edu.ut.mobileproject.core.Cloud;
import edu.ut.mobileproject.core.CloudActivity;

import java.util.Random;

public class HelloAndroidActivity extends CloudActivity {
    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(edu.ut.mobileproject.R.menu.main, menu);
	return true;
    }

    @Cloud
    public long generateRandom(long seed) throws ClassNotFoundException {
        Random rand = new Random(seed);
        return rand.nextLong();
    }

}

