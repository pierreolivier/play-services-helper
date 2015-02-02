package com.playserviceshelper.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.playserviceshelper.lib.adapters.AndroidIntentAdapter;
import com.playserviceshelper.lib.adapters.IntentAdapter;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public class AndroidNetworkWorld extends NetworkWorld implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static int RC_SIGN_IN = 9001;

    protected Activity mActivity;
    protected GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

    public AndroidNetworkWorld(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                        // add other APIs and scopes here as needed
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            return;
        }

        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if (!BaseGameUtils.resolveConnectionFailure(mActivity, mGoogleApiClient, connectionResult, RC_SIGN_IN, mActivity.getText(R.string.signin_other_error).toString())) {
                mResolvingConnectionFailure = false;
            }
        }
    }

    @Override
    public void onStart() {
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, IntentAdapter intentAdapter) {
        if (intentAdapter instanceof AndroidIntentAdapter) {
            Intent intent = ((AndroidIntentAdapter) intentAdapter).getIntent();

            if (requestCode == RC_SIGN_IN) {
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (resultCode == Activity.RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(mActivity, requestCode, resultCode, R.string.signin_failure);
                }
            }
        }
    }

    @Override
    public void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void signOutclicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
    }
}
