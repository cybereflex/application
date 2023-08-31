package cc.cybereflex.media.server.codec.model;

import io.netty.util.AsciiString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SipHeaders {

    public static final AsciiString CONTENT_LENGTH = AsciiString.cached("content-length");

    private final Map<AsciiString, String> headers = new ConcurrentHashMap<>();

    public SipHeaders add(String name, String value) {
        Assert.notNull(value, "header value cannot be null");
        Assert.isTrue(StringUtils.isNotBlank(name), "header name cannot be blank");
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            switch (c) {
                case '\u0000', '\t', '\n', '\u000b', '\f', '\r', ' ', ',', ':', ';', '=' ->
                        throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + value);
                default -> {
                    if (c > 127) {
                        throw new IllegalArgumentException("a header name cannot contain non-ASCII character: " + value);
                    }
                }
            }
        }

        this.headers.put(AsciiString.cached(name.toLowerCase()), value);
        return this;
    }

    public Optional<String> get(AsciiString name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        return Optional.of(headers.get(name));
    }

    public SipHeaders remove(String name) {
        if (StringUtils.isBlank(name)) {
            return this;
        }
        this.headers.remove(AsciiString.cached(name));
        return this;
    }
}
