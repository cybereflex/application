package cc.cybereflex.media.onvif.command;

import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
public class SystemRebootCommand extends AbstractOnvifCommand<Void> {

    private final OnvifDevice onvifDevice;

    @Override
    protected URI uri() {
        return URI.create("http://" + onvifDevice.getIp() + ":" + onvifDevice.getPort() + "/onvif/device_service");
    }

    @Override
    protected String body() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope">
                    <s:Header>%s</s:Header>
                    <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <SystemReboot xmlns="http://www.onvif.org/ver10/device/wsdl">
                        </SystemReboot>
                    </s:Body>
                </s:Envelope>
                """.formatted(buildWssHeader(onvifDevice.getUsername(), onvifDevice.getPassword()));
    }
}
