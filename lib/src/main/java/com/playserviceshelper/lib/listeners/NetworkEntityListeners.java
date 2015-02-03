package com.playserviceshelper.lib.listeners;

import com.playserviceshelper.lib.NetworkEntity;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public interface NetworkEntityListeners {
    void onMessage(NetworkEntity entity, NetworkMessage message);
}
