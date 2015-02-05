package com.playserviceshelper.lib.messages;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class MessageParser {
    public NetworkMessage onMessage(byte[] data) {
        NetworkMessage message = null;
        byte id = data[0];

        switch(id) {
            case (byte) 0xE0:
                message = new PingMessage();
                break;
            case (byte) 0xE1:
                message = new PongMessage();
                break;
            case (byte) 0xE2:
                message = new PingResultMessage(data);
                break;
        }

        return message;
    }
}
