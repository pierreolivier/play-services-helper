package com.playserviceshelper.lib;

import com.google.android.gms.games.multiplayer.Participant;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public class AndroidNetworkEntity extends NetworkEntity {
    protected Participant mParticipant;

    public AndroidNetworkEntity(Participant mParticipant) {
        this.mParticipant = mParticipant;
    }

    @Override
    public String getName() {
        return mParticipant.getDisplayName();
    }
}
