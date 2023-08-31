package cc.cybereflex.media.server.codec.decoder;

import cc.cybereflex.media.server.codec.model.*;
import cc.cybereflex.media.server.common.CommonUtil;
import cc.cybereflex.media.server.common.SipConstants;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Optional;

public class SipDecoderHelper {
    private static final int LINE_MAX_LENGTH = 4096;
    private static final AppendableCharSequence sequence = new AppendableCharSequence(128);
    private static int size = 0;

    public static Optional<SipMessage> decode(ByteBuf byteBuf) {
        //parse init line
        Optional<AppendableCharSequence> line = parseLine(byteBuf);
        if (line.isEmpty()) {
            return Optional.empty();
        }
        String[] initialLine = splitInitLine(line.get());

        //create message
        Optional<SipMessage> message = createMessage(initialLine);
        if (message.isEmpty()) {
            return Optional.empty();
        }
        SipMessage sipMessage = message.get();

        //parse header
        Optional<SipHeaders> sipHeaders = parseHeaders(byteBuf);
        if (sipHeaders.isEmpty()) {
            return Optional.empty();
        }
        sipMessage.setSipHeaders(sipHeaders.get());

        Optional<String> contentLength = sipHeaders.get().get(SipHeaders.CONTENT_LENGTH);
        if (contentLength.isEmpty()) {
            return Optional.of(sipMessage);
        }

        int bodySize = Integer.parseInt(contentLength.get());
        int readableBytes = byteBuf.readableBytes();

        if (bodySize > readableBytes) {
            throw new RuntimeException("content length is %d but readable bytes is %d".formatted(bodySize, readableBytes));
        }
        sipMessage.setContent(byteBuf.readCharSequence(bodySize, Charset.defaultCharset()));

        byteBuf.readerIndex(byteBuf.capacity());

        return Optional.of(sipMessage);
    }


    private static String[] splitHeader(AppendableCharSequence header) {
        int colonIndex = -1;
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAtUnsafe(i);
            if (c == SipConstants.COLON) {
                colonIndex = i;
                break;
            }
        }

        assert colonIndex != -1;

        return new String[]{
                header.substring(0, colonIndex).trim(),
                header.substring(colonIndex + 1, header.length()).trim()
        };
    }

    private static Optional<SipHeaders> parseHeaders(ByteBuf byteBuf) {
        Optional<AppendableCharSequence> headerLine = parseLine(byteBuf);
        if (headerLine.isEmpty()) {
            return Optional.empty();
        }

        SipHeaders sipHeaders = new SipHeaders();
        while (headerLine.isPresent() && !headerLine.get().isEmpty()) {
            String[] header = splitHeader(headerLine.get());
            sipHeaders.add(header[0], header[1]);
            headerLine = parseLine(byteBuf);
        }

        return Optional.of(sipHeaders);
    }


    private static Optional<SipMessage> createMessage(String[] initialLine) {
        if (StringUtils.equals(initialLine[2], SipVersion.SIP_2_0_STR)) {
            return Optional.of(
                    SipRequest.builder()
                            .uri(initialLine[1])
                            .sipVersion(SipVersion.SIP_2_0)
                            .sipMethod(SipMethod.valueOf(initialLine[0]))
                            .build()
            );
        } else if (StringUtils.equals(initialLine[0], SipVersion.SIP_2_0_STR)) {
            return Optional.of(
                    SipResponse.builder()
                            .sipVersion(SipVersion.SIP_2_0)
                            .responseStatus(SipResponseStatus.valueOf(Integer.parseInt(initialLine[1]), initialLine[2]))
                            .build()
            );
        } else {
            return Optional.empty();
        }
    }

    private static String[] splitInitLine(AppendableCharSequence line) {
        int firstSPIndex = -1;
        int secondSPIndex = -1;


        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (CommonUtil.isSPLenient(c)) {
                if (firstSPIndex == -1) {
                    firstSPIndex = i;
                } else if (secondSPIndex == -1) {
                    secondSPIndex = i;
                } else {
                    throw new IllegalArgumentException("init line find more SP");
                }
            }
        }

        assert firstSPIndex != -1;
        assert secondSPIndex != -1;

        return new String[]{
                line.substring(0, firstSPIndex),
                line.substring(firstSPIndex + 1, secondSPIndex),
                line.substring(secondSPIndex + 1, line.length())
        };
    }

    private static Optional<AppendableCharSequence> parseLine(ByteBuf byteBuf) {
        sequence.reset();
        size = 0;

        int i = byteBuf.forEachByte(b -> {
            char c = (char) (b & 0xFF);
            if (c == SipConstants.LF) {
                int length = sequence.length();
                if (length >= 1 && sequence.charAt(length - 1) == SipConstants.CR) {
                    size++;
                    sequence.setLength(length - 1);
                }
                return false;
            }

            if (++size > LINE_MAX_LENGTH) {
                throw new TooLongFrameException("sip line is too long");
            }

            sequence.append(c);
            return true;
        });
        if (i == -1) {
            return Optional.empty();
        }
        byteBuf.readerIndex(i + 1);
        return Optional.of(sequence);
    }
}
