package com.playserviceshelper.lib;

import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public abstract class NetworkEntity {
    protected Object mObject;
    protected NetworkEntityListeners mEntityListeners;

    public abstract String getId();
    public abstract String getName();

    public void onMessage(byte[] data) {
        if(this.mEntityListeners != null) {
            this.mEntityListeners.onMessage(this, NetworkMessage.parse(data));
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
