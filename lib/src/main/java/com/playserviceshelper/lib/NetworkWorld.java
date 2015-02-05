package com.playserviceshelper.lib;

import com.playserviceshelper.lib.adapters.IntentAdapter;
import com.playserviceshelper.lib.listeners.NetworkEntityListeners;
import com.playserviceshelper.lib.messages.MessageParser;
import com.playserviceshelper.lib.messages.NetworkMessage;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public abstract class NetworkWorld {
    protected NetworkWorldHandler mHandler;
    protected NetworkListeners mListeners;
    protected MessageParser mParser;
    // protected NetworkRoom mRoom;

    public NetworkWorld() {
        super();

        mParser = new MessageParser();
    }

    public abstract void init(NetworkConfiguration networkConfiguration);

    public abstract void onStart();
    public abstract void onStop();
    public abstract void onActivityResult(int requestCode, int resultCode, IntentAdapter intent);

    public abstract void signInClicked();
    public abstract void signOutclicked();

    public abstract void quickGame(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers);
    public abstract void invite(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers);
    public abstract void gameOver();

    public abstract void enableInvitation();
    public abstract void disableInvitation();

    public abstract void leaveRoom();

    public void onSessionStart() {
        mHandler = new NetworkWorldHandler(this);
    }

    public void onSessionEnd() {
        mHandler = null;
    }

    public NetworkListeners getListeners() {
        return mListeners;
    }
    public void setListeners(NetworkListeners mListeners) {
        this.mListeners = mListeners;
    }

    public MessageParser getParser() {
        return mParser;
    }
    public void setParser(MessageParser mParser) {
        this.mParser = mParser;
    }

    public NetworkWorldHandler getHandler() {
        return mHandler;
    }
}
