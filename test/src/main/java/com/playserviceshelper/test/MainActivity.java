package com.playserviceshelper.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.playserviceshelper.lib.*;
import com.playserviceshelper.lib.adapters.AndroidIntentAdapter;
import com.playserviceshelper.lib.listeners.HostElectionListeners;

import java.util.Arrays;

public class MainActivity extends Activity implements NetworkListeners, HostElectionListeners {

    protected NetworkWorld mNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final NetworkConfiguration configuration = new NetworkConfiguration() {
            @Override
            public String getSigninOtherError() {
                return getString(R.string.signin_other_error);
            }

            @Override
            public int getSigninFailure() {
                return R.string.signin_failure;
            }
        };

        mNetwork = new AndroidNetworkWorld(this);
        mNetwork.setListeners(this);
        mNetwork.setParser(new CustomMessageParser());
        mNetwork.init(configuration);
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

    @Override
    public void onConnected(boolean autoJoin) {
        if (!autoJoin) {
            mNetwork.enableInvitation();
            // mNetwork.invite(1, 1, 3);
            mNetwork.quickGame(1, 1, 3);
        }
    }

    @Override
    public void onInvitationReceived(final NetworkInvitation invitation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Join?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invitation.accept();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        invitation.decline();
                    }
                })
                .show();
    }

    @Override
    public void onRoomCreation() {
        Log.e("network", "onRoomCreation");
    }

    @Override
    public void onRoomError() {

    }

    @Override
    public void onSessionStart(NetworkWorld network) {
        Log.e("network", Arrays.toString(mNetwork.getHandler().getRoom().getEntities().toArray()));

        mNetwork.getHandler().startHostElection(this);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNetwork.gameOver();
            }
        }, 4000);*/
    }

    @Override
    public void onSessionEnd() {
        Log.e("network", "onSessionEnd");
    }

    @Override
    public void onHostElected(boolean isHost) {
        Log.e("network", "i'm hosting : " + isHost);
    }

    @Override
    public void onHostElectionFailed() {
        Log.e("network", "onHostElectionFailed");
    }
}
