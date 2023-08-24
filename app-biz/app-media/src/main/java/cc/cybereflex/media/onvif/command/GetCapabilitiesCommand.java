package cc.cybereflex.media.onvif.command;

import cc.cybereflex.common.component.XmlParser;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import cc.cybereflex.media.onvif.model.OnvifDeviceCapability;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

@RequiredArgsConstructor
public class GetCapabilitiesCommand extends AbstractOnvifCommand<OnvifDevice> {

    private final String ip;
    private final Integer port;
    private final String username;
    private final String password;

    @Override
    protected URI uri() {
        return URI.create("http://" + ip + ":" + port + "/onvif/device_service");
    }

    @Override
    protected String body() {
        return """
                <?xml version="1.0" encoding="utf-8"?>
                <s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope">
                    <s:Body xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                        <GetCapabilities xmlns="http://www.onvif.org/ver10/device/wsdl">
                            <Category>All</Category>
                        </GetCapabilities>
                    </s:Body>
                </s:Envelope>
                """;
    }

    @Override
    protected Result<OnvifDevice> processResponse(HttpResponse<byte[]> response) {
        Result<OnvifDevice> result = super.processResponse(response);

        if (result.isSuccess()) {
            OnvifDevice onvifDevice = OnvifDevice.builder()
                    .ip(ip)
                    .port(port)
                    .username(username)
                    .password(password)
                    .build();
            result.setData(onvifDevice);

            XmlParser.parse(new ByteArrayInputStream(response.body()), new XmlParser.AbstractSAXHandler() {
                @Override
                public void characters(char[] ch, int start, int length) {
                    Optional<XmlParser.Element> currentElement = peekCurrentElement();
                    currentElement.ifPresent(element -> {

                        String textContent = new String(ch, start, length);
                        if (StringUtils.equals("XAddr", element.getLocalName())) {
                            //获取父元素，此元素代表Onvif设备拥有的能力
                            Optional<XmlParser.Element> capabilityElement = peekParentElement();
                            capabilityElement.ifPresent(it -> {
                                switch (it.getLocalName()) {
                                    case "Events": {
                                        onvifDevice.setEventsUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.EVENTS);
                                    }
                                    case "PTZ": {
                                        onvifDevice.setPtzUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.PTZ);
                                    }
                                    case "Analytics": {
                                        onvifDevice.setAnalyticsUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.ANALYTICS);
                                    }
                                    case "Device": {
                                        onvifDevice.setDeviceUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.DEVICE);
                                    }
                                    case "Media": {
                                        onvifDevice.setMediaUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.MEDIA);
                                    }
                                    case "Imaging": {
                                        onvifDevice.setImagingUrl(textContent);
                                        onvifDevice.addCapability(OnvifDeviceCapability.IMAGING);
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        return result;
    }
}
