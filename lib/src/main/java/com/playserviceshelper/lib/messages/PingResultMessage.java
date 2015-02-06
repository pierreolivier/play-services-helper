package com.playserviceshelper.lib.messages;

import com.playserviceshelper.lib.NetworkEntity;
import com.playserviceshelper.lib.utils.Logger;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pierre-Olivier on 05/02/2015.
 */
public class PingResultMessage extends NetworkMessage {
    private byte mId = (byte) 0xE2;
    private long mResult;

    public PingResultMessage(int result, int number) {
        super();

        mResult = result;
    }

    public PingResultMessage(HashMap<NetworkEntity, List<Long>> results) {
        super();

        mResult = 0;

        for (List<Long> values : results.values()) {
            long subResult = 0;
            for (Long value : values) {
                subResult += value;
            }
            subResult /= values.size();
            mResult += subResult;
        }
    }

    public PingResultMessage(byte[] data) {
        super();

        ByteBuffer buffer = ByteBuffer.wrap(data);

        mId = buffer.get();
        mResult = buffer.getLong();
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(9);

        buffer.put(mId);
        buffer.putLong(mResult);

        return buffer.array();
    }

    public long getResult() {
        return mResult;
    }
}
