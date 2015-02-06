package com.playserviceshelper.lib.listeners;

/**
 * Created by Pierre-Olivier on 06/02/2015.
 */
public interface HostElectionListeners {
    void onHostElected(boolean isHost);
    void onHostElectionFailed();
}
