package com.playserviceshelper.lib;

import android.view.WindowManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

/**
 * Created by Pierre-Olivier on 03/02/2015.
 */
public class AndroidNetworkInvitation extends NetworkInvitation {
    protected AndroidNetworkWorld mWorld;
    protected Invitation mInvitation;

    public AndroidNetworkInvitation(AndroidNetworkWorld mWorld, Invitation mInvitation) {
        this.mWorld = mWorld;
        this.mInvitation = mInvitation;
    }

    @Override
    public void accept() {
        if (mWorld.getListeners() != null) {
            mWorld.getListeners().onRoomCreation();
        }

        RoomConfig.Builder roomConfigBuilder = mWorld.makeBasicRoomConfigBuilder();
        roomConfigBuilder.setInvitationIdToAccept(mInvitation.getInvitationId());
        Games.RealTimeMultiplayer.join(mWorld.getGoogleApiClient(), roomConfigBuilder.build());

        mWorld.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void decline() {
        Games.RealTimeMultiplayer.declineInvitation(mWorld.getGoogleApiClient(), mInvitation.getInvitationId());
    }
}
