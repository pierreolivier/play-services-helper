package com.playserviceshelper.lib;

import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class NetworkWorldHandler  implements NetworkEntityListeners {
    protected NetworkWorld mWorld;
    protected NetworkRoom mRoom;

    public NetworkWorldHandler(NetworkWorld networkWorld) {
        super();

        mWorld = networkWorld;
    }

    @Override
    public void onMessage(NetworkEntity entity, NetworkMessage message) {

    }

    public NetworkRoom getRoom() {
        return mRoom;
    }

    public void setRoom(NetworkRoom mRoom) {
        this.mRoom = mRoom;
    }

    public void startHostElection() {

    }
}
