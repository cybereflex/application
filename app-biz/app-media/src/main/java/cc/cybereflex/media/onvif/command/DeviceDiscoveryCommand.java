package cc.cybereflex.media.onvif.command;

import cc.cybereflex.common.enums.ResultEnum;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.common.DiscoveryCallback;
import lombok.RequiredArgsConstructor;

import java.net.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DeviceDiscoveryCommand extends AbstractOnvifCommand<Void> {

    private final DiscoveryCallback callback;
    private final long timeout;
    private final TimeUnit timeoutUnit;
    private final int waitTime;

    private static final String IPV4_MULTICAST = "239.255.255.250";
    private static final String IPV6_MULTICAST = "[FF02::C]";
    private static final int WS_DISCOVERY_PORT = 3702;

    @Override
    public Result<Void> execute() {
        try {
            List<InetAddress> addresses = new ArrayList<>();

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual()) {
                    continue;
                }

                addresses.addAll(
                        networkInterface.getInterfaceAddresses().stream()
                                .map(InterfaceAddress::getAddress)
                                .toList()
                );
            }

            callback.onDiscoveryStarted();
            CountDownLatch countDownLatch = new CountDownLatch(addresses.size());
            addresses.forEach(it -> CompletableFuture.runAsync(() -> {
                int port = new SecureRandom().nextInt(20000) + 40000;
                try (DatagramSocket socket = new DatagramSocket(port, it)) {
                    socket.setBroadcast(true);
                    if (it instanceof Inet4Address) {
                        socket.send(new DatagramPacket(body().getBytes(), body().length(), InetAddress.getByName(IPV4_MULTICAST), WS_DISCOVERY_PORT));
                    } else if (it instanceof Inet6Address) {
                        socket.send(new DatagramPacket(body().getBytes(), body().length(), InetAddress.getByName(IPV6_MULTICAST), WS_DISCOVERY_PORT));
                    } else {
                        throw new IllegalArgumentException("illegal inet address: " + it.getHostName());
                    }

                    long startTime = System.currentTimeMillis();
                    while (System.currentTimeMillis() - startTime > timeoutUnit.toMillis(timeout)) {
                        DatagramPacket receiveData = new DatagramPacket(new byte[4096], 4096);
                        socket.receive(receiveData);
                        socket.setSoTimeout(waitTime);
                        String response = new String(receiveData.getData());
                        callback.onDeviceFound(receiveData.getAddress().getHostName(), response);
                    }
                } catch (Exception ignore) {

                } finally {
                    countDownLatch.countDown();
                }
            }));
            callback.onDiscoveryFinished(countDownLatch.await(timeout, timeoutUnit));
        } catch (Exception e) {
            callback.onError(e);
        }

        return Result.success(ResultEnum.SUCCESS);
    }

    @Override
    protected URI uri() {
        return null;
    }

    @Override
    protected String body() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <Envelope xmlns="http://www.w3.org/2003/05/soap-envelope" xmlns:tds="http://www.onvif.org/ver10/device/wsdl">
                    <Header>
                        <wsa:MessageID xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">uuid:%s</wsa:MessageID>
                        <wsa:To xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To>
                        <wsa:Action xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action>
                    </Header>
                    <Body>
                        <Probe xmlns="http://schemas.xmlsoap.org/ws/2005/04/discovery" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                            <Types>tds:Device</Types>
                            <Scopes/>
                        </Probe>
                    </Body>
                </Envelope>
                """
                .formatted(UUID.randomUUID().toString());
    }

}
