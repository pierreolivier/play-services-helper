package com.playserviceshelper.lib;

import com.playserviceshelper.lib.adapters.IntentAdapter;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public abstract class NetworkWorld {
    public abstract void init();

    public abstract void onStart();
    public abstract void onStop();

    public abstract void onActivityResult(int requestCode, int resultCode, IntentAdapter intent);

    public abstract void signInClicked();
    public abstract void signOutclicked();
}
