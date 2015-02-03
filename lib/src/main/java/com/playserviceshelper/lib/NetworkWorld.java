package com.playserviceshelper.lib;

import com.playserviceshelper.lib.adapters.IntentAdapter;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public abstract class NetworkWorld {
    protected NetworkListeners mListeners;
    protected NetworkRoom mRoom;

    public abstract void init(NetworkConfiguration networkConfiguration);

    public void setListeners(NetworkListeners mListeners) {
        this.mListeners = mListeners;
    }

    public abstract void onStart();
    public abstract void onStop();
    public abstract void onActivityResult(int requestCode, int resultCode, IntentAdapter intent);

    public abstract void signInClicked();
    public abstract void signOutclicked();

    public abstract void quickGame(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers);
    public abstract void invite(int variant, int minAutoMatchPlayers, int maxAutoMatchPlayers);

    public abstract void enableInvitation();
    public abstract void disableInvitation();

    public abstract void leaveRoom();

    public NetworkRoom getRoom() {
        return mRoom;
    }
}
