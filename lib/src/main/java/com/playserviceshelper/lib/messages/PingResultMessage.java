package com.playserviceshelper.lib.messages;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class PingResultMessage extends NetworkMessage {
    private byte mId = (byte) 0xE2;
    private int mResult;

    public PingResultMessage(int result) {
        super();

        mResult = result;
    }

    public PingResultMessage(byte[] data) {
        super();

        ByteBuffer buffer = ByteBuffer.wrap(data);

        mId = buffer.get();
        mResult = buffer.getInt();
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        buffer.put(mId);
        buffer.putInt(mResult);

        return buffer.array();
    }

    public int getResult() {
        return mResult;
    }
}
