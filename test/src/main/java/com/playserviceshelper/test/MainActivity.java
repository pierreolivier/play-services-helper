package com.playserviceshelper.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.playserviceshelper.lib.AndroidNetworkWorld;
import com.playserviceshelper.lib.NetworkWorld;
import com.playserviceshelper.lib.adapters.AndroidIntentAdapter;

public class MainActivity extends Activity {

    protected NetworkWorld mNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetwork = new AndroidNetworkWorld(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mNetwork.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mNetwork.onStop();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        mNetwork.onActivityResult(requestCode, resultCode, new AndroidIntentAdapter(intent));
    }
}
