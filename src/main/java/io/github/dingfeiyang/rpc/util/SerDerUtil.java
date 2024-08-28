package io.github.dingfeiyang.rpc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerDerUtil {

    public synchronized static byte[] ser(Object msg) {
        byte[] msgBody = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(msg);
            msgBody = out.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        }

        return msgBody;
    }
}
