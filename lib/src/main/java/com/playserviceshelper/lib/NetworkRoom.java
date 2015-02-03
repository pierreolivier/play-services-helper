package com.playserviceshelper.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkRoom {
    protected HashMap<String, NetworkEntity> mEntities;
    protected NetworkEntity mCreator;

    public NetworkRoom() {
        super();

        mEntities = new HashMap<String, NetworkEntity>();
    }

    public abstract String getId();

    public NetworkEntity getCreator() {
        return mCreator;
    }

    public NetworkEntity getEntity(String id) {
        return mEntities.get(id);
    }

    public Collection<NetworkEntity> getEntities() {
        return mEntities.values();
    }
}
