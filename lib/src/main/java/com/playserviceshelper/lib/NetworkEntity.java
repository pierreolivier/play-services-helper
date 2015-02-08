package com.playserviceshelper.lib;

import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkEntity {
    protected NetworkRoom mRoom;
    protected Object mObject;
    protected NetworkEntityListeners mEntityListeners;

    public NetworkEntity(NetworkRoom room) {
        super();

        mRoom = room;
    }

    public abstract String getId();
    public abstract String getName();

    public abstract void sendReliableMessage(byte[] message);
    public abstract void sendReliableMessage(NetworkMessage message);
    public abstract void sendUnreliableMessage(byte[] message);
    public abstract void sendUnreliableMessage(NetworkMessage message);

    public void onMessage(NetworkMessage message) {
        if(mEntityListeners != null) {
            mEntityListeners.onMessage(this, message);
        }
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object mObject) {
        this.mObject = mObject;
    }

    public void setEntityListeners(NetworkEntityListeners mEntityListeners) {
        this.mEntityListeners = mEntityListeners;
    }

    public String toString() {
        return "NetworkEntity [name=" + getName() + "]";
    }
}
