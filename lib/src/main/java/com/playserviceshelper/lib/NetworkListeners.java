package com.playserviceshelper.lib;

/**
 * Created by Pierre-Olivier on 02/02/2015.
 */
public interface NetworkListeners {
    void onConnected(boolean autoJoin);

    void onInvitationReceived(NetworkInvitation invitation);

    void onRoomCreation();
    void onRoomError();

    void onSessionStart(NetworkWorld network);
    void onSessionEnd();

}
