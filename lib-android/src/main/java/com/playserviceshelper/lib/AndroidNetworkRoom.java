package com.playserviceshelper.lib;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public class AndroidNetworkRoom extends NetworkRoom {
    protected AndroidNetworkWorld mWorld;
    protected Room mRoom;

    private GoogleApiClient mGoogleApiClient;
    private String mRoomId;

    public AndroidNetworkRoom(AndroidNetworkWorld world, Room mRoom) {
        super();

        this.mWorld = world;
        this.mRoom = mRoom;

        this.mGoogleApiClient = world.getGoogleApiClient();
        this.mRoomId = mRoom.getRoomId();

        for(String id : mRoom.getParticipantIds()) {
            Participant participant = mRoom.getParticipant(id);

            NetworkEntity entity = new AndroidNetworkEntity(this, participant);

            mEntities.put(id, entity);

            if (id.equals(mRoom.getCreatorId())) {
                mCreator = entity;
            }
        }
    }

    @Override
    public void broadcastReliableMessage(NetworkMessage message) {
        byte[] data = message.serialize();

        for (NetworkEntity entity : mEntities.values()) {
            entity.sendReliableMessage(data);
        }
    }

    @Override
    public void broadcastUnreliableMessage(NetworkMessage message) {
        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, message.serialize(), mRoom.getRoomId());
    }

    @Override
    public String getId() {
        return mRoomId;
    }

    public AndroidNetworkWorld getWorld() {
        return mWorld;
    }
}
