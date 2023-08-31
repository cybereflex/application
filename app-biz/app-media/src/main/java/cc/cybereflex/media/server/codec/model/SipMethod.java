package cc.cybereflex.media.server.codec.model;

import io.netty.util.AsciiString;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SipMethod {
    /**
     * OPTIONS
     */
    public static final SipMethod OPTIONS = new SipMethod("OPTIONS");

    /**
     * REGISTER
     */
    public static final SipMethod REGISTER = new SipMethod("REGISTER");

    /**
     * INVITE
     */
    public static final SipMethod INVITE = new SipMethod("INVITE");

    /**
     * ACK
     */
    public static final SipMethod ACK = new SipMethod("ACK");

    /**
     * CANCEL
     */
    public static final SipMethod CANCEL = new SipMethod("CANCEL");

    /**
     * BYE
     */
    public static final SipMethod BYE = new SipMethod("BYE");

    /**
     * SUBSCRIBE
     */
    public static final SipMethod SUBSCRIBE = new SipMethod("SUBSCRIBE");

    /**
     * NOTIFY
     */
    public static final SipMethod NOTIFY = new SipMethod("NOTIFY");

    /**
     * MESSAGE
     */
    public static final SipMethod MESSAGE = new SipMethod("MESSAGE");

    /**
     * INFO
     */
    public static final SipMethod INFO = new SipMethod("INFO");


    private static final Map<String, SipMethod> METHOD_MAP = new ConcurrentHashMap<>();

    static {
        METHOD_MAP.put(OPTIONS.toString(), OPTIONS);
        METHOD_MAP.put(REGISTER.toString(), REGISTER);
        METHOD_MAP.put(INVITE.toString(), INVITE);
        METHOD_MAP.put(ACK.toString(), ACK);
        METHOD_MAP.put(CANCEL.toString(), CANCEL);
        METHOD_MAP.put(BYE.toString(), BYE);
        METHOD_MAP.put(SUBSCRIBE.toString(), SUBSCRIBE);
        METHOD_MAP.put(NOTIFY.toString(), NOTIFY);
        METHOD_MAP.put(MESSAGE.toString(), MESSAGE);
        METHOD_MAP.put(INFO.toString(), INFO);
    }

    public static SipMethod valueOf(String name) {
        SipMethod result = METHOD_MAP.get(name);
        return Objects.nonNull(result) ? result : new SipMethod(name);
    }

    private final AsciiString name;

    public SipMethod(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("blank name");
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }

        this.name = AsciiString.cached(name);
    }
}
