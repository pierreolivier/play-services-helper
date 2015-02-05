package com.playserviceshelper.lib.messages;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class PingMessage extends NetworkMessage {
    private byte mId = (byte) 0xE0;
    private long mTime;
    private byte[] mData = new byte[32];

    public PingMessage(long time) {
        super();

        mTime = time;

        for (int i = 0; i < mData.length ; i++) {
            mData[i] = (byte) i;
        }
    }

    public PingMessage(byte[] data) {
        super();

        ByteBuffer buffer = ByteBuffer.wrap(data);

        mId = buffer.get();
        mTime = buffer.getLong();
        buffer.get(mData, 0, 32);
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(41);

        buffer.put(mId);
        buffer.putLong(mTime);
        buffer.put(mData);

        return buffer.array();
    }

    public long getTime() {
        return mTime;
    }
}
