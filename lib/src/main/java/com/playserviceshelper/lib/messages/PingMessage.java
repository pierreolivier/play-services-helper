package com.playserviceshelper.lib.messages;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class PingMessage extends NetworkMessage {
    private byte mId = (byte) 0xE0;
    private byte[] mData = new byte[32];

    public PingMessage() {
        super();

        for (int i = 0; i < mData.length ; i++) {
            mData[i] = (byte) i;
        }
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(33);

        buffer.put(mId);
        buffer.put(mData);

        return buffer.array();
    }
}
