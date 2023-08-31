package cc.cybereflex.media.server.codec.model;

import io.jsonwebtoken.lang.Assert;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.util.internal.ObjectUtil.checkPositiveOrZero;

@Data
public class SipVersion{

    private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");

    public static final String SIP_2_0_STR = "SIP/2.0";
    public static final SipVersion SIP_2_0 = new SipVersion("SIP", 2, 0);


    public static SipVersion valueOf(String text) {
        if (StringUtils.isBlank(text)){
            throw new IllegalArgumentException("blank text");
        }

        return new SipVersion(text.trim());
    }

    private final String protocolName;
    private final int majorVersion;
    private final int minorVersion;
    private final String text;

    public SipVersion(String text) {
        if (StringUtils.isBlank(text)){
            throw new IllegalArgumentException("blank sip version");
        }

        Matcher m = VERSION_PATTERN.matcher(text);
        if (!m.matches()) {
            throw new IllegalArgumentException("invalid version format: " + text);
        }

        this.protocolName = m.group(1);
        this.majorVersion = Integer.parseInt(m.group(2));
        this.minorVersion = Integer.parseInt(m.group(3));
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
    }

    public SipVersion(String protocolName, int majorVersion, int minorVersion) {
        if (StringUtils.isBlank(protocolName)){
            throw new IllegalArgumentException("blank protocol name");
        }

        protocolName = protocolName.trim().toUpperCase();

        for (int i = 0; i < protocolName.length(); i++) {
            if (Character.isISOControl(protocolName.charAt(i)) || Character.isWhitespace(protocolName.charAt(i))) {
                throw new IllegalArgumentException("invalid character in protocol name");
            }
        }

        Assert.isTrue(majorVersion >= 0, "major version must bigger than zero");
        Assert.isTrue(minorVersion >= 0, "minor version must bigger than zero");

        this.protocolName = protocolName;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.text = protocolName + '/' + majorVersion + '.' + minorVersion;
    }

}
