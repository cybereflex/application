package cc.cybereflex.media.server.codec.model;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.util.internal.ObjectUtil.checkPositiveOrZero;

public class SipVersion implements Comparable<SipVersion> {

    private static final Pattern VERSION_PATTERN =
            Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");

    public static final String SIP_2_0_STRING = "SIP/2.0";

    public static final SipVersion SIP_2_0 = new SipVersion("SIP", 2, 0, true);


    public static SipVersion valueOf(String text) {
        ObjectUtil.checkNotNull(text, "text");
        text = text.trim();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("sip text is empty");
        }

        SipVersion version = version0(text);
        if (version == null) {
            version = new SipVersion(text);
        }
        return version;
    }

    private static SipVersion version0(String text) {
        if (SIP_2_0_STRING.equals(text)) {
            return SIP_2_0;
        }
        return null;
    }


    private final String protocolName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;
    private final byte[] bytes;

    public SipVersion(String text) {
        ObjectUtil.checkNotNull(text, "text");
        text = text.trim().toUpperCase();
        if (text.isEmpty()) {
            throw new IllegalArgumentException("empty text");
        }
        Matcher m = VERSION_PATTERN.matcher(text);
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text);
        }
        protocolName = m.group(1);
        majorVersion = Integer.parseInt(m.group(2));
        minorVersion = Integer.parseInt(m.group(3));
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
        bytes = null;
    }

    public SipVersion(String protocolName, int majorVersion, int minorVersion) {
        this(protocolName, majorVersion, minorVersion, false);
    }

    public SipVersion(String protocolName, int majorVersion, int minorVersion, boolean bytes) {
        ObjectUtil.checkNotNull(protocolName, "protocolName");

        protocolName = protocolName.trim().toUpperCase();
        if (protocolName.isEmpty()) {
            throw new IllegalArgumentException("empty protocolName");
        }

        for (int i = 0; i < protocolName.length(); i++) {
            if (Character.isISOControl(protocolName.charAt(i)) ||
                    Character.isWhitespace(protocolName.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocolName");
            }
        }

        checkPositiveOrZero(majorVersion, "majorVersion");
        checkPositiveOrZero(minorVersion, "minorVersion");

        this.protocolName = protocolName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        text = protocolName + '/' + majorVersion + '.' + minorVersion;

        if (bytes) {
            this.bytes = text.getBytes(CharsetUtil.US_ASCII);
        } else {
            this.bytes = null;
        }
    }

    public String protocolName() {
        return protocolName;
    }

    public int majorVersion() {
        return majorVersion;
    }

    public int minorVersion() {
        return minorVersion;
    }

    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return text();
    }

    @Override
    public int hashCode() {
        return (protocolName().hashCode() * 31 + majorVersion()) * 31 +
                minorVersion();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SipVersion that)) {
            return false;
        }

        return minorVersion() == that.minorVersion() &&
                majorVersion() == that.majorVersion() &&
                protocolName().equals(that.protocolName());
    }

    @Override
    public int compareTo(SipVersion o) {
        int v = protocolName().compareTo(o.protocolName());
        if (v != 0) {
            return v;
        }

        v = majorVersion() - o.majorVersion();
        if (v != 0) {
            return v;
        }

        return minorVersion() - o.minorVersion();
    }

    void encode(ByteBuf buf) {
        if (bytes == null) {
            buf.writeCharSequence(text, CharsetUtil.US_ASCII);
        } else {
            buf.writeBytes(bytes);
        }
    }
}
