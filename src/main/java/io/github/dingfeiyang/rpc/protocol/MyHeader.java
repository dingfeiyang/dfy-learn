package io.github.dingfeiyang.rpc.protocol;

import java.io.Serializable;
import java.util.UUID;

public class MyHeader implements Serializable {
    int flag;
    long requestId;
    long dataLen;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }

    public static MyHeader createHeader(byte[] msg) {
        MyHeader header = new MyHeader();
        int size = msg.length;
        int flag = 0x14141414;
        long requestId = Math.abs(UUID.randomUUID().getLeastSignificantBits());

        header.setDataLen(size);
        header.setFlag(flag);
        header.setRequestId(requestId);

        return header;
    }
}
