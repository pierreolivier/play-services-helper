package com.playserviceshelper.lib;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public class AndroidNetworkRoom extends NetworkRoom {
    protected Room mRoom;

    public AndroidNetworkRoom(Room mRoom) {
        super();

        this.mRoom = mRoom;

        for(String id : mRoom.getParticipantIds()) {
            Participant participant = mRoom.getParticipant(id);

            NetworkEntity entity = new AndroidNetworkEntity(participant);

            mEntities.put(id, entity);

            if (id.equals(mRoom.getCreatorId())) {
                mCreator = entity;
            }
        }
    }

    @Override
    public String getId() {
        return mRoom.getRoomId();
    }
}
