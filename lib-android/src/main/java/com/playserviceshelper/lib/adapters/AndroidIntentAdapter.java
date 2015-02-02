package com.playserviceshelper.lib.adapters;

import android.content.Intent;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public class AndroidIntentAdapter extends IntentAdapter {
    protected Intent mIntent;

    public AndroidIntentAdapter(Intent mIntent) {
        this.mIntent = mIntent;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent mIntent) {
        this.mIntent = mIntent;
    }
}
