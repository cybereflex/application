package cc.cybereflex.media.onvif.command;

import cc.cybereflex.common.component.XmlParser;
import cc.cybereflex.common.model.Result;
import cc.cybereflex.media.onvif.common.AbstractOnvifCommand;
import cc.cybereflex.media.onvif.model.OnvifDevice;
import cc.cybereflex.media.onvif.model.OnvifDeviceInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

@RequiredArgsConstructor
public class GetDeviceInfoCommand extends AbstractOnvifCommand<OnvifDeviceInfo> {

    private final OnvifDevice onvifDevice;

    @Override
    protected URI uri() {
        return URI.create(onvifDevice.getDeviceUrl());
    }

    @Override
    protected String body() {
        return """
                 <?xml version="1.0" encoding="utf-8"?>
                 <s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope" xmlns:tds="http://www.onvif.org/ver10/device/wsdl" xmlns:tt="http://www.onvif.org/ver10/schema">
                     <s:Header>%s</s:Header>
                     <s:Body>
                         <tds:GetDeviceInformation />
                     </s:Body>
                 </s:Envelope>
                """
                .formatted(buildWssHeader(onvifDevice.getUsername(), onvifDevice.getPassword()))
                ;
    }


    @Override
    protected Result<OnvifDeviceInfo> processResponse(HttpResponse<byte[]> response) {
        Result<OnvifDeviceInfo> result = super.processResponse(response);

        if (result.isSuccess()) {
            OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
            result.setData(onvifDeviceInfo);

            XmlParser.parse(new ByteArrayInputStream(response.body()), new XmlParser.AbstractSAXHandler() {

                @Override
                public void characters(char[] ch, int start, int length) {
                    Optional<XmlParser.Element> currentElement = peekCurrentElement();

                    currentElement.ifPresent(element -> {
                        String textContent = new String(ch, start, length);

                        if (StringUtils.equals(element.getLocalName(), "Manufacturer")) {
                            onvifDeviceInfo.setManufacturer(textContent);
                        }
                        if (StringUtils.equals(element.getLocalName(), "FirmwareVersion")) {
                            onvifDeviceInfo.setFirmwareVersion(textContent);
                        }
                        if (StringUtils.equals(element.getLocalName(), "Model")) {
                            onvifDeviceInfo.setModel(textContent);
                        }
                        if (StringUtils.equals(element.getLocalName(), "SerialNumber")) {
                            onvifDeviceInfo.setSerialNumber(textContent);
                        }
                        if (StringUtils.equals(element.getLocalName(), "HardwareId")) {
                            onvifDeviceInfo.setHardwareId(textContent);
                        }
                    });
                }
            });
        }

        return result;
    }
}
