package cc.cybereflex.media;


import cc.cybereflex.media.server.SipServer;
import cc.cybereflex.media.server.SipServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);

        SipServerConfig config = new SipServerConfig();
        SipServer server = new SipServer(config);
        new Thread(server::startTcp).start();
        new Thread(server::startUdp).start();
    }
}
