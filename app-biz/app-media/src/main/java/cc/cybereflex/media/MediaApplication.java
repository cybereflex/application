package cc.cybereflex.media;


import cc.cybereflex.media.server.MediaServer;
import cc.cybereflex.media.server.MediaServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);

        MediaServerConfig config = new MediaServerConfig();
        MediaServer server = new MediaServer(config);
        new Thread(server::startTcp).start();
        new Thread(server::startUdp).start();
    }
}
