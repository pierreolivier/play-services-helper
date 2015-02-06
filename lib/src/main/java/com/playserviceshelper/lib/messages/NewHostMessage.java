package com.playserviceshelper.lib.messages;

import com.playserviceshelper.lib.utils.Serializer;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 06/02/2015.
 */
public class NewHostMessage extends NetworkMessage {
    private byte mId = (byte) 0xE3;
    private String mHostId;

    public NewHostMessage(String mHostId) {
        super();

        this.mHostId = mHostId;
    }

    public NewHostMessage(byte[] data) {
        super();

        ByteBuffer buffer = ByteBuffer.wrap(data);

        mId = buffer.get();
        mHostId = Serializer.getString(buffer);
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(5 + mHostId.length());

        buffer.put(mId);
        Serializer.putString(buffer, mHostId);

        return buffer.array();
    }

    public String getHostId() {
        return mHostId;
    }
}
