package cc.cybereflex.media.server.codec.decoder;

import cc.cybereflex.media.server.codec.model.SipMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Optional;

public class SipUdpDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf byteBuf = datagramPacket.content();

        Optional<SipMessage> message = SipDecoderHelper.decode(byteBuf);
        if (message.isEmpty()){
            return;
        }
        list.add(message.get());
    }
}
