package cc.cybereflex.media;


import cc.cybereflex.common.component.Json;
import cc.cybereflex.media.server.codec.decoder.SipTcpDecoder;
import cc.cybereflex.media.server.codec.model.SipMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

public class DecoderTest {
    @Test
    public void decode(){
        byte[] data1 = """
                REGISTER sip:192.168.30.183:5060 SIP/2.0
                Via: SIP/2.0/UDP 192.168.30.183:5061;rport;branch=z9hG4bK2995420819
                From: <sip:35010401401127000000@3501040140>;tag=4112720925
                To: <sip:35010401401127000000@3501040140>
                Call-ID: 667210238
                CSeq: 1 REGISTER
                Contact: <sip:35010401401127000000@172.16.0.102:5061>
                Max-Forwards: 70
                User-Agent: HIC PUSHER
                Expires: 30
                Content-Length: 5
                
                HELLO
                """
                .getBytes();

        byte[] data2 = """
                SIP/2.0 401 Unauthorized
                Via: SIP/2.0/UDP 192.168.30.183:5061;rport=5061;branch=z9hG4bK2995420819
                From: <sip:35010401401127000000@3501040140>;tag=4112720925
                To: <sip:35010401401127000000@3501040140>;tag=4157052765
                Call-ID: 667210238
                CSeq: 1 REGISTER
                User-Agent: HIC SERVER
                WWW-Authenticate: Digest realm=0,algorithm=MD5,nonce=1234567890,stale=false
                Content-Length: 0
                """
                .getBytes();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new SipTcpDecoder()
        );

        embeddedChannel.writeInbound(Unpooled.wrappedBuffer(data1), Unpooled.wrappedBuffer(data2));

        SipMessage msg1 = embeddedChannel.readInbound();
        SipMessage msg2 = embeddedChannel.readInbound();

        System.out.println(Json.writeValueAsString(msg1));
        System.out.println(Json.writeValueAsString(msg2));
    }
}
