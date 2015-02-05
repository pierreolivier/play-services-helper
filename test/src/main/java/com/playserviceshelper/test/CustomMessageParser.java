package com.playserviceshelper.test;

import com.playserviceshelper.lib.messages.*;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class CustomMessageParser extends MessageParser {
    @Override
    public NetworkMessage onMessage(byte[] data) {
        NetworkMessage message = super.onMessage(data);

        if(message == null) {
            byte id = data[0];
            switch(id) {
                case (byte) 0x00:
                    message = new PingMessage();
                    break;
            }
        }

        return message;
    }
}
