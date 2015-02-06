package com.playserviceshelper.lib;

import com.playserviceshelper.lib.messages.NetworkMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkRoom {
    protected HashMap<String, NetworkEntity> mEntities;
    protected String mIdPlayer;
    protected NetworkEntity mCreator;

    public NetworkRoom() {
        super();

        mEntities = new HashMap<String, NetworkEntity>();
        mIdPlayer = "";
        mCreator = null;
    }

    public abstract void broadcastReliableMessage(NetworkMessage message);
    public abstract void broadcastUnreliableMessage(NetworkMessage message);

    public abstract String getId();

    public String getIdPlayer() {
        return mIdPlayer;
    }

    public void setIdPlayer(String mIdPlayer) {
        this.mIdPlayer = mIdPlayer;
    }

    public NetworkEntity getCreator() {
        return mCreator;
    }

    public NetworkEntity getEntity(String id) {
        return mEntities.get(id);
    }

    public Collection<NetworkEntity> getEntities() {
        return mEntities.values();
    }

    public int getSize() {
        return mEntities.values().size();
    }
}
