package com.playserviceshelper.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.playserviceshelper.lib.adapters.AndroidIntentAdapter;
import com.playserviceshelper.lib.adapters.IntentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public class AndroidNetworkWorld extends NetworkWorld implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener, OnInvitationReceivedListener {
    private final static int RC_SIGN_IN = 9001;
    private final static int RC_SELECT_PLAYERS = 10000;
    private final static int RC_WAITING_ROOM = 10002;

    protected Activity mActivity;
    protected GoogleApiClient mGoogleApiClient;
    protected Room mRoom;

    protected NetworkConfiguration mConfiguration;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;

    public AndroidNetworkWorld(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void init(NetworkConfiguration configuration) {
        mConfiguration = configuration;

        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        boolean autoJoin = false;

        if (bundle != null) {
            Invitation inv = bundle.getParcelable(Multiplayer.EXTRA_INVITATION);

            if (inv != null) {
                // accept mInvitation
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId());
                Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

                // prevent screen from sleeping during handshake
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // go to game screen
                autoJoin = true;
            }
        }

        if(mListeners != null) {
            mListeners.onConnected(autoJoin);
        }
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

            if (!BaseGameUtils.resolveConnectionFailure(mActivity, mGoogleApiClient, connectionResult, RC_SIGN_IN, mConfiguration.getSigninOtherError())) {
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
                    BaseGameUtils.showActivityResultError(mActivity, requestCode, resultCode, mConfiguration.getSigninFailure());
                }
            } else if (requestCode == RC_SELECT_PLAYERS) {
                if (resultCode != Activity.RESULT_OK) {
                    // user canceled
                    return;
                }

                // get the invitee list
                Bundle extras = intent.getExtras();
                final ArrayList<String> invitees = intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

                // get auto-match criteria
                Bundle autoMatchCriteria = null;
                int minAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
                int maxAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

                if (minAutoMatchPlayers > 0) {
                    autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);
                } else {
                    autoMatchCriteria = null;
                }

                // create the room and specify a variant if appropriate
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.addPlayersToInvite(invitees);
                if (autoMatchCriteria != null) {
                    roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
                }
                RoomConfig roomConfig = roomConfigBuilder.build();
                Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

                // prevent screen from sleeping during handshake
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else if (requestCode == RC_WAITING_ROOM) {
                if (resultCode == Activity.RESULT_OK) {
                    if(mListeners != null) {
                        mListeners.onStartSession();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // Waiting room was dismissed with the back button. The meaning of this
                    // action is up to the game. You may choose to leave the room and cancel the
                    // match, or do something else like minimize the waiting room and
                    // continue to connect in the background.

                    // in this example, we take the simple approach and just leave the room:
                    // Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player wants to leave the room.
                    // Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    @Override
    public void quickGame(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers) {
        Bundle bundle = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);

        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setVariant(variant);
        roomConfigBuilder.setAutoMatchCriteria(bundle);
        RoomConfig roomConfig = roomConfigBuilder.build();

        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void invite(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers) {
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, minAutoMatchPlayers, maxAutoMatchPlayers);
        mActivity.startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void enableInvitation() {
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);
    }

    @Override
    public void disableInvitation() {
        Games.Invitations.unregisterInvitationListener(mGoogleApiClient);
    }

    public RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
            Log.e("network", "onRoomCreated error");

            if(mListeners != null) {
                mListeners.onRoomError();
            }
        } else {
            Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
            mActivity.startActivityForResult(i, RC_WAITING_ROOM);
        }
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
            Log.e("network", "onJoinedRoom error");

            if(mListeners != null) {
                mListeners.onRoomError();
            }
        } else {
            Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
            mActivity.startActivityForResult(i, RC_WAITING_ROOM);
        }
    }

    @Override
    public void onLeftRoom(int statusCode, String roomId) {

    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // show error message, return to main screen.
            Log.e("network", "onRoomConnected error");

            if(mListeners != null) {
                mListeners.onRoomError();
            }
        } else {
            mRoom = room;
        }
    }

    @Override
    public void onRoomConnecting(Room room) {

    }

    @Override
    public void onRoomAutoMatching(Room room) {

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {

    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.e("network", "onPeerDeclined");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {

    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.e("network", "onPeerLeft");
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.e("network", "onPeersConnected");
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.e("network", "onPeersDisconnected");
    }

    @Override
    public void onConnectedToRoom(Room room) {

    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoom = null;
    }

    @Override
    public void onP2PConnected(String participantId) {

    }

    @Override
    public void onP2PDisconnected(String participantId) {

    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {

    }

    @Override
    public void onInvitationReceived(final Invitation invitation) {
        Log.e("network", "onInvitationReceived");

        if(mListeners != null) {
            mListeners.onInvitationReceived(new AndroidNetworkInvitation(this, invitation));
        }
    }

    @Override
    public void onInvitationRemoved(String invitationId) {

    }

    public Activity getActivity() {
        return mActivity;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public Room getRoom() {
        return mRoom;
    }

    public NetworkConfiguration getConfiguration() {
        return mConfiguration;
    }
}
