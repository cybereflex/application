package cc.cybereflex.media.server;

import lombok.Data;

@Data
public class SipServerConfig {

    /**
     * udp 端口
     */
    private Integer udpPort = 9999;

    /**
     * tcp 端口
     */
    private Integer tcpPort = 9999;
}
