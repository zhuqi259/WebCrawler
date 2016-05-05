package cn.edu.jlu.webcrawler.sms.autoeasy.util;

public class Base64 {

    public static char[] encode(byte[] data) {
        char[] out = new char[(((data.length + 0x2) / 0x3) * 0x4)];
        for (int i = 0x0, index = 0x0; i < data.length; i = i + 0x3, index = index + 0x4) {
            boolean quad = false;
            boolean trip = false;
            int val = data[i] & 0xff;
            val = val << 0x8;
            if ((i + 0x1) < data.length) {
                val |= (data[(i + 0x1)] & 0xff);
                trip = true;
            }
            val = val << 0x8;
            if ((i + 0x2) < data.length) {
                val |= (data[(i + 0x2)] & 0xff);
                quad = true;
            }
            out[(index + 0x3)] = alphabet[quad ? (val & 0x3f) : 0x40];
            val = val >> 0x6;
            out[(index + 0x2)] = alphabet[trip ? (val & 0x3f) : 0x40];
            val = val >> 0x6;
            out[(index + 0x1)] = alphabet[(val & 0x3f)];
            val = val >> 0x6;
            out[(index)] = alphabet[(val & 0x3f)];
        }
        return out;
    }

    public static byte[] decode(char[] data) throws Exception {
        int len = ((data.length + 0x3) / 0x4) * 0x3;
        if ((data.length > 0) && (data[(data.length - 0x1)] == 0x3d)) {
            len = len - 0x1;
        }
        if ((data.length > 0x1) && (data[(data.length - 0x2)] == 0x3d)) {
            len = len - 0x1;
        }
        byte[] out = new byte[len];
        int shift = 0x0;
        int accum = 0x0;
        int index = 0x0;
        for (int ix = 0x0; ix < data.length; ix = ix + 0x1) {
            byte value = codes[(data[ix] & 0xff)];
            if (value >= 0) {
                accum = accum << 0x6;
                shift = shift + 0x6;
                accum |= value;
                if (shift >= 0x8) {
                    shift = shift - 0x8;
                    out[(index++)] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }
        if (index != out.length) {
            throw new Exception("miscalculated data length!");
        }
        return out;
    }

    private static byte[] codes = new byte[0x100];

    static {
        for (int i = 0x0; i < 0x100; i = i + 0x1) {
            codes[i] = -0x1;
        }
        for (int i = 0x41; i <= 0x5a; i = i + 0x1) {
            codes[i] = (byte) (i - 0x41);
        }
        for (int i = 0x61; i <= 0x7a; i = i + 0x1) {
            codes[i] = (byte) ((i + 0x1a) - 0x61);
        }
        for (int i = 0x30; i <= 0x39; i = i + 0x1) {
            codes[i] = (byte) ((i + 0x34) - 0x30);
        }
        codes[0x2b] = 0x3e;
        codes[0x2f] = 0x3f;
    }

    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
}
