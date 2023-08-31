package cc.cybereflex.media.server.common;

import io.netty.util.internal.AppendableCharSequence;

public class CommonUtil {

    public static boolean isSPLenient(char c){
        return c == ' ' || c == (char) 0x09 || c == (char) 0x0B || c == (char) 0x0C || c == (char) 0x0D;
    }


}
