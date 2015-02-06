package com.playserviceshelper.lib;

import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public class AndroidNetworkEntity extends NetworkEntity {
    protected AndroidNetworkRoom mAndroidRoom;
    protected Participant mParticipant;

    private GoogleApiClient mGoogleApiClient;
    private String mRoomId;

    public AndroidNetworkEntity(AndroidNetworkRoom room, Participant mParticipant) {
        super(room);

        this.mAndroidRoom = room;
        this.mParticipant = mParticipant;

        this.mGoogleApiClient = room.getWorld().getGoogleApiClient();
        this.mRoomId = room.getId();
    }

    @Override
    public String getId() {
        return mParticipant.getParticipantId();
    }

    @Override
    public String getName() {
        return mParticipant.getDisplayName();
    }

    @Override
    public void sendReliableMessage(final byte[] message) {
        Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, new RealTimeMultiplayer.ReliableMessageSentCallback() {
            @Override
            public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {
                Log.e("network", "sendReliableMessage : " + statusCode + " message id: " + Integer.toHexString(message[0]));
            }
        }, message, mRoomId, mParticipant.getParticipantId());
    }

    @Override
    public void sendReliableMessage(NetworkMessage message) {
        sendReliableMessage(message.serialize());
    }

    @Override
    public void sendUnreliableMessage(byte[] message) {
        Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, message, mRoomId, mParticipant.getParticipantId());
    }

    @Override
    public void sendUnreliableMessage(NetworkMessage message) {
        sendUnreliableMessage(message.serialize());
    }
}
