package com.playserviceshelper.lib;

import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.NetworkMessage;
import com.playserviceshelper.lib.messages.PingMessage;
import com.playserviceshelper.lib.messages.PongMessage;
import com.playserviceshelper.lib.utils.Logger;
import sun.rmi.runtime.Log;

import java.util.HashMap;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class NetworkWorldHandler  implements NetworkEntityListeners {
    protected NetworkWorld mWorld;
    protected NetworkRoom mRoom;
    protected String mId;

    protected NetworkEntity mHostEntity;
    protected boolean mHostElection;
    final protected HashMap<NetworkEntity, Long> mHostResults;

    public NetworkWorldHandler(NetworkWorld networkWorld) {
        super();

        mWorld = networkWorld;
        mRoom = null;

        mHostEntity = null;
        mHostElection = false;
        mHostResults = new HashMap<NetworkEntity, Long>();
    }

    @Override
    public void onMessage(NetworkEntity entity, NetworkMessage message) {
        if (message instanceof PingMessage) {
            PingMessage pingMessage = (PingMessage) message;

            entity.sendUnreliableMessage(new PongMessage(entity.getId(), pingMessage.getTime()));
        } else if(message instanceof PongMessage) {
            PongMessage pongMessage = (PongMessage) message;
            long time = System.currentTimeMillis();

            synchronized (mHostResults) {
                Long result = mHostResults.get(entity);
                if (result == null) {
                    result = time - pongMessage.getTime();
                } else {
                    result += time - pongMessage.getTime();
                }
                mHostResults.put(entity, result);
            }

            mId = pongMessage.getParticipantId();
        }
    }

    public NetworkRoom getRoom() {
        return mRoom;
    }

    public void setRoom(NetworkRoom mRoom) {
        this.mRoom = mRoom;
    }

    public void startHostElection() {
        if (mHostEntity == null && !mHostElection) {
            mHostElection = true;

            for (int i = 0 ; i < 10 ; i++) {
                PingMessage pingMessage = new PingMessage(System.currentTimeMillis());
                mRoom.broadcastUnreliableMessage(pingMessage);
            }
        }
    }
}
