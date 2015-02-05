package com.playserviceshelper.lib.messages;

import com.playserviceshelper.lib.utils.Serializer;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class PongMessage extends NetworkMessage {
    private byte mId = (byte) 0xE1;
    private String mParticipantId;
    private long mTime;
    private byte[] mData = new byte[32];

    public PongMessage(String participantId, long time) {
        super();

        mParticipantId = participantId;
        mTime = time;

        for (int i = 0; i < mData.length ; i++) {
            mData[i] = (byte) i;
        }
    }

    public PongMessage(byte[] data) {
        super();

        ByteBuffer buffer = ByteBuffer.wrap(data);

        mId = buffer.get();
        mParticipantId = Serializer.getString(buffer);
        mTime = buffer.getLong();
        buffer.get(mData, 0, 32);
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(45 + mParticipantId.length());

        buffer.put(mId);
        Serializer.putString(buffer, mParticipantId);
        buffer.putLong(mTime);
        buffer.put(mData);

        return buffer.array();
    }

    public long getTime() {
        return mTime;
    }

    public String getParticipantId() {
        return mParticipantId;
    }
}
