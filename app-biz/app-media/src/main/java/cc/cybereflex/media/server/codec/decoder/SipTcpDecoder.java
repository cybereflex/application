package cc.cybereflex.media.server.codec.decoder;

import cc.cybereflex.media.server.codec.model.SipMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Optional;

public class SipTcpDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

        Optional<SipMessage> message = SipDecoderHelper.decode(byteBuf);
        if (message.isEmpty()){
            return;
        }
        list.add(message.get());
    }

}
