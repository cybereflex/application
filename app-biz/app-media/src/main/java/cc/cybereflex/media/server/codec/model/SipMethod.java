package cc.cybereflex.media.server.codec.model;

import io.netty.util.AsciiString;

import java.util.HashMap;
import java.util.Map;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

public class SipMethod implements Comparable<SipMethod> {
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

    /**
     * UPDATE
     */
    public static final SipMethod UPDATE = new SipMethod("UPDATE");

    /**
     * PUBLISH
     */
    public static final SipMethod PUBLISH = new SipMethod("PUBLISH");

    /**
     * PRACK
     */
    public static final SipMethod PRACK = new SipMethod("PRACK");

    /**
     * REFER
     */
    public static final SipMethod REFER = new SipMethod("REFER");


    private static final Map<String, SipMethod> METHOD_MAP = new HashMap<>();

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
        METHOD_MAP.put(UPDATE.toString(), UPDATE);
        METHOD_MAP.put(PUBLISH.toString(), PUBLISH);
        METHOD_MAP.put(PRACK.toString(), PRACK);
        METHOD_MAP.put(REFER.toString(), REFER);
    }

    public static SipMethod valueOf(String name) {
        SipMethod result = METHOD_MAP.get(name);
        return result != null ? result : new SipMethod(name);
    }

    private final AsciiString name;

    public SipMethod(String name) {
        name = checkNotNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }

        this.name = AsciiString.cached(name);
    }

    public String name() {
        return name.toString();
    }


    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SipMethod that)) {
            return false;
        }

        return name().equals(that.name());
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public int compareTo(SipMethod o) {
        if (o == this) {
            return 0;
        }
        return name().compareTo(o.name());
    }
}
