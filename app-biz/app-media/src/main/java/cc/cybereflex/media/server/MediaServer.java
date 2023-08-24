package cc.cybereflex.media.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class MediaServer {
    private static final Logger logger = LoggerFactory.getLogger(MediaServer.class);

    private final MediaServerConfig config;

    public void startTcp() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline();
                        }
                    });


            ChannelFuture channelFuture = bootstrap.bind(config.getTcpPort()).sync();
            channelFuture.channel().closeFuture().await();
        } catch (InterruptedException e) {
            logger.error("media server listen on tcp failed", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void startUdp() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline();
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(config.getUdpPort()).sync();
            channelFuture.channel().closeFuture().await();

        } catch (InterruptedException e) {
            logger.error("media server listen on udp failed", e);
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

}
