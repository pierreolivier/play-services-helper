package com.playserviceshelper.lib.utils;

import java.nio.ByteBuffer;

/**
 * Created by Pierre-Olivier on 04/02/2015.
 */
public class Serializer {
    public static void putString(ByteBuffer buffer, String string) {
        byte[] data = string.getBytes();

        buffer.putInt(data.length);
        buffer.put(data);
    }

    public static String getString(ByteBuffer buffer) {
        int size = buffer.getInt();
        byte[] data = new byte[size];

        buffer.get(data, 0, size);

        return new String(data);
    }


    public static int size(Object... objects) {
        int size = 0;
        for (Object object : objects) {
            if (object instanceof Byte) size += 1;
            else if (object instanceof Short) size += 2;
            else if (object instanceof Integer) size += 4;
            else if (object instanceof Float) size += 4;
            else if (object instanceof Long) size += 8;
            else if (object instanceof Double) size += 8;
            else if (object instanceof Character) size += 2;
            else if (object instanceof Boolean) size += 1;
        }
        return size;
    }

    public static int size(Class... types) {
        int size = 0;
        for (Class type : types) {
            if (type == Byte.class) size += 1;
            else if (type == Short.class) size += 2;
            else if (type == Integer.class) size += 4;
            else if (type == Float.class) size += 4;
            else if (type == Long.class) size += 8;
            else if (type == Double.class) size += 8;
            else if (type == Character.class) size += 2;
            else if (type == Boolean.class) size += 1;
        }
        return size;
    }

    public static byte[] serialize(byte id, Object... objects) {
        int size = size(objects) + 1;
        byte[] result = new byte[size];

        ByteBuffer b = ByteBuffer.allocate(4);

        result[0] = id;

        int i = 1;
        for (Object object : objects) {
            if (object instanceof Byte) {
                result[i] = (Byte) object;
                i += 1;
            } else if (object instanceof Short) {
                short x = (Short) object;

                result[i] = (byte)(x & 0xff);
                result[i+1] = (byte)((x >> 8) & 0xff);

                i += 2;
            } else if (object instanceof Integer) {
                int x = (Integer) object;

                result[i] = (byte) (x >> 24);
                result[i+1] = (byte) (x >> 16);
                result[i+2] = (byte) (x >> 8);
                result[i+3] = (byte) (x);

                i += 4;
            } else if (object instanceof Float) {
                int x = Float.floatToIntBits((Float) object);

                result[i] = (byte)(x & 0xff);
                result[i+1] = (byte)((x >> 8) & 0xff);
                result[i+2] = (byte)((x >> 16) & 0xff);
                result[i+3] = (byte)((x >> 24) & 0xff);

                i += 4;
            } else if (object instanceof Long) {
                long x = (Long) object;

                result[i] = (byte)(x >>> 56);
                result[i+1] = (byte)(x >>> 48);
                result[i+2] = (byte)(x >>> 40);
                result[i+3] = (byte)(x >>> 32);
                result[i+4] = (byte)(x >>> 24);
                result[i+5] = (byte)(x >>> 16);
                result[i+6] = (byte)(x >>>  8);
                result[i+7] = (byte)(x);

                i += 8;
            } else if (object instanceof Double) {
                long x = Double.doubleToLongBits((Double) object);

                result[i] = (byte)(x >>> 56);
                result[i+1] = (byte)(x >>> 48);
                result[i+2] = (byte)(x >>> 40);
                result[i+3] = (byte)(x >>> 32);
                result[i+4] = (byte)(x >>> 24);
                result[i+5] = (byte)(x >>> 16);
                result[i+6] = (byte)(x >>> 8);
                result[i+7] = (byte)(x);

                i += 8;
            } else if (object instanceof Character) {
                char x = (Character) object;

                result[i] = (byte)(x >> 8);
                result[i+1] = (byte)(x);

                i += 2;
            } else if (object instanceof Boolean) {
                boolean x = (Boolean) object;

                result[i] = (byte) (x ? 0xFF : 0x00);

                i += 1;
            }
        }

        return result;
    }
}
