package com.playserviceshelper.lib;

import java.util.ArrayList;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkRoom {
    protected ArrayList<NetworkEntity> mEntities;
    protected NetworkEntity mCreator;

    public NetworkRoom() {
        super();

        mEntities = new ArrayList<NetworkEntity>();
    }

    public NetworkEntity getCreator() {
        return mCreator;
    }

    public ArrayList<NetworkEntity> getEntities() {
        return mEntities;
    }
}
